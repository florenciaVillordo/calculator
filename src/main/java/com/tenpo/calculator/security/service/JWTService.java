package com.tenpo.calculator.security.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JWTService {
    /**
     *
     */
    private static final Map<String, List<String>> JWTTokenBlacklistMap = new ConcurrentHashMap<>();

    @Value("${tenpo.app.jwt.secret:secret}")
    private String jwtSecret;
    @Value("${tenpo.app.jwt.expiration.ms:60000}")
    private int jwtExpirationMs;


    public String getJWTToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512,
                        jwtSecret.getBytes()).compact();
    }


    private Date getTokenExpirationDate(String token) {
        return getTokenClaim(token, Claims::getExpiration);
    }

    private <T> T getTokenClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = getAllTokenClaims(token);
        return claimsResolver.apply(claims);
    }

    public String getUsernameFromToken(String token) {
        return getTokenClaim(token, Claims::getSubject);
    }

    private Claims getAllTokenClaims(String token) {
        return Jwts.parser().setSigningKey(jwtSecret.getBytes()).parseClaimsJws(token).getBody();
    }

    private boolean isTokenExpired(String token) {
        Date expiration = getTokenExpirationDate(token);
        return expiration.before(new Date());
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && isValidToken(username, token));
    }

    private boolean isValidToken(String username, String token) {
        boolean isBlacklistedToken = JWTTokenBlacklistMap.containsKey(username)
                && JWTTokenBlacklistMap.get(username).contains(token);

        return !isTokenExpired(token) && !isBlacklistedToken;
    }

    /**
     * Invalidate JWT token for logout.
     * Save the JWT in a blacklist cache so as the same JWT cannot be used anymore
     *
     * @param token token to invalidate
     */
    public void invalidateToken(String token) {
        String username = getUsernameFromToken(token);
        JWTTokenBlacklistMap.putIfAbsent(username, new ArrayList<>());
        JWTTokenBlacklistMap.computeIfPresent(username, (key, currentList) -> {
            currentList.add(token);
            return currentList;
        });
    }
}

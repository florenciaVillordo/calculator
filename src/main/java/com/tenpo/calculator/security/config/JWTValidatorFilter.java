package com.tenpo.calculator.security.config;

import com.tenpo.calculator.security.service.JWTService;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Filter to authenticate user request using jwt token
 *
 * @author Florencia Villordo
 */
@Slf4j
@RequiredArgsConstructor
@AllArgsConstructor
public class JWTValidatorFilter extends OncePerRequestFilter {

    private JWTService JWTService;
    private UserDetailsService userDetailsService;


    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws
            ServletException, IOException {
        String token = httpServletRequest.getHeader("Authorization");
        String username = getUsernameFromToken(token);
        authenticateWithToken(username, token, httpServletRequest);

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private String getUsernameFromToken(String token) {
        if (token != null) {
            try {
                return JWTService.getUsernameFromToken(token);
            } catch (SignatureException e) {
                log.error("Invalid JWT signature: {}", e.getMessage());
            } catch (MalformedJwtException e) {
                log.error("Invalid JWT token: {}", e.getMessage());
            } catch (ExpiredJwtException e) {
                log.error("JWT token is expired: {}", e.getMessage());
            } catch (UnsupportedJwtException e) {
                log.error("JWT token is unsupported: {}", e.getMessage());
            } catch (IllegalArgumentException e) {
                log.error("JWT claims string is empty: {}", e.getMessage());
            }
        }

        return null;
    }

    private void authenticateWithToken(String username, String token, HttpServletRequest httpServletRequest) {
        if (username != null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (JWTService.validateToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));

                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
    }
}

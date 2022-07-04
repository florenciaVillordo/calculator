package com.tenpo.calculator.security.service;

import com.tenpo.calculator.exception.LoginFailedException;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginService {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JWTService jwtService;


    public String login(String username, String password) {
        UserDetails userDetails = loadUserByUsername(username);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(username, password, userDetails.getAuthorities());
        authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        if (usernamePasswordAuthenticationToken.isAuthenticated()) {
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            log.debug("User {} Login successfully", username);
            return jwtService.getJWTToken(username);
        }
        String message = String.format("User {} Login %s failed", username);
        log.debug(message);
        throw new LoginFailedException(message);
    }


    public void logout(String token) {
        jwtService.invalidateToken(token);
    }

    private UserDetails loadUserByUsername(String username) {
        Optional<com.tenpo.calculator.security.model.User> userOp = userService.findByUsername(username);
        if (userOp.isEmpty()) {
            UsernameNotFoundException ex = new UsernameNotFoundException(username);
            log.error(ex.getMessage(), ex);
            throw ex;
        }
        com.tenpo.calculator.security.model.User user = userOp.get();
        Set<GrantedAuthority> grantedAuthorities =
                user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getType().name())).collect(Collectors.toSet());
        return new User(user.getUsername(), user.getPassword(), grantedAuthorities);
    }
}

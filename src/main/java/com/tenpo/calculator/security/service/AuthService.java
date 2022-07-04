package com.tenpo.calculator.security.service;

import com.tenpo.calculator.security.dto.SingUpDto;
import com.tenpo.calculator.security.dto.UserDto;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final LoginService loginService;
    private final UserService userService;

    public void signIn(SingUpDto userDto) {
        userService.save(userDto);
    }

    public String login(UserDto userDto) {
        return loginService.login(userDto.getUsername(), userDto.getPassword());
    }

    public void logout(String token) {
        loginService.logout(token);
    }
}

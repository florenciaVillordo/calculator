package com.tenpo.calculator.security.controller;

import com.tenpo.calculator.controller.CalculatorConstant;
import com.tenpo.calculator.security.SecurityConstant;
import com.tenpo.calculator.security.dto.SingUpDto;
import com.tenpo.calculator.security.dto.UserDto;
import com.tenpo.calculator.security.service.AuthService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthService service;


    @PostMapping(CalculatorConstant.SIGN_UP_URI)
    @ResponseStatus(HttpStatus.OK)
    public void signup(@RequestBody @Valid SingUpDto request) {
        service.signIn(request);
    }

    @PostMapping(CalculatorConstant.LOGIN_URI)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> login(@RequestBody @Valid UserDto userDto) {
        String token = service.login(userDto);
        return ResponseEntity.ok(token);
    }


    @GetMapping({CalculatorConstant.LOGOUT_URI})
    @ResponseStatus(HttpStatus.OK)
    public void logout(@RequestHeader(SecurityConstant.AUTHORIZATION) String token) {
        service.logout(token);
    }
}

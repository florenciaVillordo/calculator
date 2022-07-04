package com.tenpo.calculator.exception;

public class LoginFailedException extends RuntimeException{

    public LoginFailedException(String message) {
        super(message);
    }
}

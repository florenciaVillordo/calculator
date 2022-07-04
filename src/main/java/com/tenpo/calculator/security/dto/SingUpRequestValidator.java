package com.tenpo.calculator.security.dto;

import com.tenpo.calculator.security.service.UserService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class SingUpRequestValidator implements ConstraintValidator<SingUpRequestValid, SingUpDto> {

    private final UserService userService;

    public SingUpRequestValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean isValid(SingUpDto s, ConstraintValidatorContext context) {
        if (userService.findByUsername(s.getUsername()).isPresent()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Username already exists").addConstraintViolation();
            return false;
        }

        if (passwordsAreNotEqual(s)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Passwords must be equal").addConstraintViolation();
            return false;
        }
        return true;
    }

    private static boolean passwordsAreNotEqual(SingUpDto s) {
        return !s.getPassword().equals(s.getConfirmPassword());
    }
}

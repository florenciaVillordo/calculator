package com.tenpo.calculator.security.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@SingUpRequestValid
@Getter
@RequiredArgsConstructor
public class SingUpDto extends UserDto {

    private String confirmPassword;
}

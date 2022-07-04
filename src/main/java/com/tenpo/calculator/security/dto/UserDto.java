package com.tenpo.calculator.security.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@SuperBuilder
@Getter
@RequiredArgsConstructor
public class UserDto {

    @NotNull(message = "Name may not be null")
    @NotBlank(message = "Name may not be Blank")
    @NotEmpty(message = "Name may not be Empty")
    private String username;
    @NotNull(message = "password may not be null")
    @NotBlank(message = "password may not be Blank")
    @NotEmpty(message = "password may not be Empty")
    private String password;

}

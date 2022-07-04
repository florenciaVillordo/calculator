package com.tenpo.calculator.security.dto;

import com.tenpo.calculator.security.model.Role;
import com.tenpo.calculator.security.model.RoleType;

import java.util.Set;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class UserCreatedDto {
    private String username;
    private String password;
    private Set<RoleType> roleTypes;
}

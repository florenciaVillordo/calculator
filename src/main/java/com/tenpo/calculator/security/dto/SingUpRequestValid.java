package com.tenpo.calculator.security.dto;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(
        validatedBy = {SingUpRequestValidator.class}
)
@Documented
public @interface SingUpRequestValid {
    String message() default "Invalid SingUp parameters";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

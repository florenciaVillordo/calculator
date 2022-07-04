package com.tenpo.calculator.exception;

import org.springframework.http.HttpStatus;

import java.util.Date;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Builder
@Getter
@Setter
public class ErrorDetail {
    private Date timestamp;
    private String message;
    private List<String> details;
    private HttpStatus httpStatus;
}

package com.tenpo.calculator.controller;

import com.tenpo.calculator.dto.ResponseDto;
import com.tenpo.calculator.service.CalculatorService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CalculatorController {

    private final CalculatorService calculatorService;

    @GetMapping({CalculatorConstant.ADD_URI})
    public ResponseEntity<ResponseDto> add(@Valid @NotNull @RequestParam BigDecimal firstNumber,
            @Valid @NotNull @RequestParam BigDecimal secondNumber) {
        BigDecimal result = calculatorService.add(firstNumber, secondNumber);
        ResponseDto responseDto = ResponseDto.builder().result(result).build();
        return ResponseEntity.ok(responseDto);
    }
}

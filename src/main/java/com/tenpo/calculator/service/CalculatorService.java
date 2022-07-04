package com.tenpo.calculator.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import lombok.extern.slf4j.Slf4j;


/**
 * Service in charge of perform calculator operations
 *
 * @author FVillordo
 */
@Service
@Slf4j
public class CalculatorService {


    /**
     * sum firstNumber and secondNumber
     * @param firstNumber number to sum
     * @param secondNumber number to sum
     * @return firstNumber + secondNumber
     */
    public BigDecimal add(BigDecimal firstNumber, BigDecimal secondNumber) {
        log.debug("Doing add operation for parameters: {} + {}", firstNumber, secondNumber);
        return firstNumber.add(secondNumber);
    }
}

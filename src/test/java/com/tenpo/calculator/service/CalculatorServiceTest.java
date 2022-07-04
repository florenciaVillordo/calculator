package com.tenpo.calculator.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;

/**
 * Unit tests for {@link CalculatorService}.
 */
@RunWith(MockitoJUnitRunner.class)
public class CalculatorServiceTest {

    @InjectMocks
    private CalculatorService service;

    @Test
    public void testAdd_Succeed() {
        assertEquals(BigDecimal.valueOf(4.0), service.add(BigDecimal.valueOf(2.0), BigDecimal.valueOf(2.0)));
        assertEquals(BigDecimal.valueOf(2.0), service.add(BigDecimal.valueOf(4.0), BigDecimal.valueOf(-2.0)));
    }
}

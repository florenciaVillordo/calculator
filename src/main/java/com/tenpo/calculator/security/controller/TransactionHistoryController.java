package com.tenpo.calculator.security.controller;


import com.tenpo.calculator.controller.CalculatorConstant;
import com.tenpo.calculator.dto.TxHistoryDto;
import com.tenpo.calculator.interceptor.TransactionName;
import com.tenpo.calculator.model.TxResult;
import com.tenpo.calculator.service.MetricRecorderService;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(CalculatorConstant.TRANSACTIONS_URI)
@RequiredArgsConstructor
public class TransactionHistoryController {

    private final MetricRecorderService service;


    @GetMapping
    public ResponseEntity<Page<TxHistoryDto>> history(@RequestParam(defaultValue = "0") Integer offset,
            @RequestParam(defaultValue = "10") Integer limit,
            @RequestParam(defaultValue = "id") String orderBy,
            @RequestParam(required = false) TxResult result,
            @RequestParam(name = "txType", required = false) TransactionName transactionType) {
        return ResponseEntity.ok(service.getPage(offset, limit, orderBy, result, transactionType));
    }
}

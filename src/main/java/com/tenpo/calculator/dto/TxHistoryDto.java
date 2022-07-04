package com.tenpo.calculator.dto;

import com.tenpo.calculator.interceptor.TransactionName;
import com.tenpo.calculator.model.TxResult;

import java.time.OffsetDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class TxHistoryDto {

    private OffsetDateTime createDate;

    private TransactionName txType;

    private String userName;

    private TxResult result;
}

package com.tenpo.calculator.controller;

import com.google.common.collect.ImmutableMap;
import com.tenpo.calculator.interceptor.TransactionName;

import java.util.Map;

public interface CalculatorConstant {

    String AUTH_URI = "/auth";
    String LOGIN_URI = "/login";
    String LOGOUT_URI = "/logout";
    String SIGN_UP_URI = "/signup";
    String AUTH_LOGIN_URI = AUTH_URI + LOGIN_URI;
    String AUTH_LOGOUT_URI = AUTH_URI + LOGOUT_URI;
    String AUTH_SIGN_UP_URI = AUTH_URI + SIGN_UP_URI;
    String ADD_URI = "/add";
    String TRANSACTIONS_URI = "/transactions";

    Map<String, TransactionName> metricNameMap= ImmutableMap.of(
            AUTH_LOGIN_URI, TransactionName.LOGIN,
            AUTH_LOGOUT_URI, TransactionName.LOGOUT,
            AUTH_SIGN_UP_URI, TransactionName.SIGN_UP,
            ADD_URI + "/", TransactionName.ADD,
            TRANSACTIONS_URI, TransactionName.TRANSACTIONS

    );
}

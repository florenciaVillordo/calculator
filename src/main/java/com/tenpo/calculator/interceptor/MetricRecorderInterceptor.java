package com.tenpo.calculator.interceptor;



import com.tenpo.calculator.controller.CalculatorConstant;
import com.tenpo.calculator.model.TxResult;
import com.tenpo.calculator.service.MetricRecorderService;

import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


/**
 * Interceptor in charge of record transaction history
 *
 * @author Florencia Villordo
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class MetricRecorderInterceptor implements HandlerInterceptor {

    private final MetricRecorderService metricRecorderService;

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) {
        if (response.getStatus() < 300) {
            recordTransaction(request.getRequestURI(), TxResult.SUCCESS);
        } else {
            recordTransaction(request.getRequestURI(), TxResult.FAILURE);
        }
    }

    private TransactionName getTransactionName(String name){
        if (CalculatorConstant.metricNameMap.get(name) != null){
            return CalculatorConstant.metricNameMap.get(name);
        }
        log.warn("Request Uri {} could not map in Transaction name", name);
        return TransactionName.OTHER;
    }

    private void recordTransaction(String transactionName, TxResult result) {
        metricRecorderService.recordTx(getTransactionName(transactionName), result);
    }



}

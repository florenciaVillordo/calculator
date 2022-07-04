package com.tenpo.calculator.service;

import com.tenpo.calculator.model.TxHistory;
import com.tenpo.calculator.model.TxResult;
import com.tenpo.calculator.repository.TxRepository;
import com.tenpo.calculator.security.model.User;
import com.tenpo.calculator.security.service.UserService;

import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MetricRecorderService {
    private final TxRepository repository;
    private final UserService service;

    /**
     * Record a transaction with its result and the authenticated user,
     * its executions in asynchronous
     *
     * @param txType
     * @param result SUCCESS or FAILURE
     */
    @Async
    public void recordTx(String txType, TxResult result) {
        TxHistory txHistory = TxHistory.builder().txType(txType).result(result).build();
        Optional<User> user = service.findByUsername(getAuthenticatedUsername());
        user.ifPresent(txHistory::setUser);
        repository.save(txHistory);
    }

    private static String getAuthenticatedUsername() {
        Authentication authentication = getContextAuthentication();
        return authentication.getName();
    }

    private static Authentication getContextAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}

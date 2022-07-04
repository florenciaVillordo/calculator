package com.tenpo.calculator.service;

import com.tenpo.calculator.dto.TxHistoryDto;
import com.tenpo.calculator.interceptor.TransactionName;
import com.tenpo.calculator.model.TxHistory;
import com.tenpo.calculator.model.TxResult;
import com.tenpo.calculator.repository.TxRepository;
import com.tenpo.calculator.security.model.User;
import com.tenpo.calculator.security.service.UserService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Function;

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
    public void recordTx(TransactionName txType, TxResult result) {
        TxHistory txHistory = TxHistory.builder().txType(txType).result(result).build();
        Optional<User> user = service.findByUsername(getAuthenticatedUsername());
        user.ifPresent(txHistory::setUser);
        repository.save(txHistory);
    }

    public Page<TxHistoryDto> getPage(Integer offset, Integer limit, String orderBy,
            TxResult result, TransactionName transactionName) {
        Pageable pageable = PageRequest.of(offset, limit, Sort.by(orderBy));

        if (checkFilter(result) && checkFilter(transactionName)) {
            return map(repository
                    .findByTxTypeAndResult(pageable, transactionName, result));
        }

        if (transactionName != null) {
            return map(repository.findByTxType(pageable, transactionName));
        }

        if (checkFilter(result)) {
            return map(repository.findByResult(pageable, result));
        }

        return map(repository.findAll(pageable));

    }


    private Page<TxHistoryDto> map(Page<TxHistory> txHistoryPage) {
        return txHistoryPage.map(new Function<TxHistory, TxHistoryDto>() {
            @Override
            public TxHistoryDto apply(TxHistory entity) {
                return TxHistoryDto.builder().createDate(entity.getCreateDate())
                        .userName(entity.getUser() != null ? entity.getUser().getUsername() : null)
                        .result(entity.getResult())
                        .txType(entity.getTxType()).build();
            }
        });

    }


    private boolean checkFilter(Enum aEnum) {
        return aEnum != null;
    }

    private static String getAuthenticatedUsername() {
        Authentication authentication = getContextAuthentication();
        return authentication.getName();
    }

    private static Authentication getContextAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}

package com.tenpo.calculator.repository;

import com.tenpo.calculator.interceptor.TransactionName;
import com.tenpo.calculator.model.TxHistory;
import com.tenpo.calculator.model.TxResult;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TxRepository extends PagingAndSortingRepository<TxHistory, Long> {
    Page<TxHistory> findByTxType(Pageable pageable, TransactionName transactionName);

    Page<TxHistory> findByResult(Pageable pageable, TxResult result);

    Page<TxHistory> findByTxTypeAndResult(Pageable pageable, TransactionName transactionName,
            TxResult status);
}

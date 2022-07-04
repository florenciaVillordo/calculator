package com.tenpo.calculator.repository;

import com.tenpo.calculator.model.TxHistory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TxRepository extends JpaRepository<TxHistory, Long> {
}

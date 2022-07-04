package com.tenpo.calculator.service;


import com.tenpo.calculator.dto.TxHistoryDto;
import com.tenpo.calculator.interceptor.TransactionName;
import com.tenpo.calculator.model.TxResult;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@SqlGroup({@Sql(scripts = "classpath:test_transaction_history.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)})
public class MetricRecorderServiceIT {
    @Autowired
    private MetricRecorderService metricRecorderService;

    @Test
    public void test_without_filters() {

        Page<TxHistoryDto> page = metricRecorderService.getPage(0, 10, "id", null, null);

        Assertions.assertThat(page.getTotalElements()).isEqualTo(4L);
        Assertions.assertThat(page.get().count()).isEqualTo(4L);
    }

    @Test public void test_with_status_filter() {
        Page<TxHistoryDto> page = metricRecorderService.getPage(0, 10, "id", TxResult.SUCCESS, null);
        Assertions.assertThat(page.getTotalElements()).isEqualTo(2L);
        Assertions.assertThat(page.get().count()).isEqualTo(2L);

    }

    @Test public void test_with_transaction_name_filter() {
        Page<TxHistoryDto> page = metricRecorderService.getPage(0, 10, "id", null, TransactionName.LOGIN);
        Assertions.assertThat(page.getTotalElements()).isEqualTo(2L);
        Assertions.assertThat(page.get().count()).isEqualTo(2L);
    }

    @Test public void test_with_transaction_name_and_status_filter() {
        Page<TxHistoryDto> page = metricRecorderService.getPage(0, 10, "id", TxResult.SUCCESS, TransactionName.LOGIN);
        Assertions.assertThat(page.getTotalElements()).isEqualTo(1L);
        Assertions.assertThat(page.get().count()).isEqualTo(1L);

    }
}

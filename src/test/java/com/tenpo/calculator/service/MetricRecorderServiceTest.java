package com.tenpo.calculator.service;


import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.tenpo.calculator.interceptor.TransactionName;
import com.tenpo.calculator.model.TxHistory;
import com.tenpo.calculator.model.TxResult;
import com.tenpo.calculator.repository.TxRepository;
import com.tenpo.calculator.security.model.User;
import com.tenpo.calculator.security.service.UserService;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

/**
 * Unit tests for {@link MetricRecorderService}.
 */

@RunWith(MockitoJUnitRunner.class)
public class MetricRecorderServiceTest {

    @InjectMocks
    private MetricRecorderService metricRecorderService;

    @Mock
    private TxRepository repository;

    @Mock
    private UserService userService;

    @Before
    public void setup() {
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    public void test_recordTxWithUser() {
        User user = User.builder().username("name").build();
        Mockito.when(userService.findByUsername(ArgumentMatchers.any())).thenReturn(Optional.of(user));
        metricRecorderService.recordTx(TransactionName.ADD, TxResult.FAILURE);
        ArgumentCaptor<TxHistory> txHistoryArgumentCaptor = ArgumentCaptor.forClass(TxHistory.class);
        verify(repository, times(1)).save(txHistoryArgumentCaptor.capture());
        TxHistory txHistory = txHistoryArgumentCaptor.getValue();
        Assertions.assertThat(txHistory.getResult()).isEqualTo(TxResult.FAILURE);
        Assertions.assertThat(txHistory.getUser()).isEqualTo(user);
    }

    @Test
    public void test_recordTxWithoutUser() {
        Mockito.when(userService.findByUsername(ArgumentMatchers.any())).thenReturn(Optional.empty());
        metricRecorderService.recordTx(TransactionName.ADD, TxResult.SUCCESS);
        ArgumentCaptor<TxHistory> txHistoryArgumentCaptor = ArgumentCaptor.forClass(TxHistory.class);
        verify(repository, times(1)).save(txHistoryArgumentCaptor.capture());
        TxHistory txHistory = txHistoryArgumentCaptor.getValue();
        Assertions.assertThat(txHistory.getResult()).isEqualTo(TxResult.SUCCESS);
        Assertions.assertThat(txHistory.getUser()).isNull();
    }



}

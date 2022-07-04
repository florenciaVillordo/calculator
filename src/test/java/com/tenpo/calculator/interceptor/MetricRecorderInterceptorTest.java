package com.tenpo.calculator.interceptor;


import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import com.tenpo.calculator.model.TxResult;
import com.tenpo.calculator.service.MetricRecorderService;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletContext;


/**
 * Unit tests for {@link MetricRecorderInterceptor}.
 */
@RunWith(MockitoJUnitRunner.class)
public class MetricRecorderInterceptorTest {
    @InjectMocks
    private MetricRecorderInterceptor interceptor;

    @Mock
    private MetricRecorderService metricRecorderService;


    @Test
    public void testAfterCompletionFailureRequest() {
        MockHttpServletRequest req = get("/add")
                .buildRequest(new MockServletContext());
        MockHttpServletResponse resp = new MockHttpServletResponse();
        resp.setStatus(400);
        interceptor.afterCompletion(req, resp, null, null);

        ArgumentCaptor<TxResult> txResultArgumentCaptor = ArgumentCaptor.forClass(TxResult.class);
        verify(metricRecorderService, times(1)).recordTx(eq("/add"), txResultArgumentCaptor.capture());
        Assertions.assertThat(txResultArgumentCaptor.getValue()).isEqualTo(TxResult.FAILURE);
    }

    @Test
    public void testAfterCompletionSuccessRequest() {
        MockHttpServletRequest req = get("/add")
                .buildRequest(new MockServletContext());
        MockHttpServletResponse resp = new MockHttpServletResponse();
        resp.setStatus(200);
        interceptor.afterCompletion(req, resp, null, null);

        ArgumentCaptor<TxResult> txResultArgumentCaptor = ArgumentCaptor.forClass(TxResult.class);
        verify(metricRecorderService, times(1)).recordTx(eq("/add"), txResultArgumentCaptor.capture());
        Assertions.assertThat(txResultArgumentCaptor.getValue()).isEqualTo(TxResult.SUCCESS);
    }
}

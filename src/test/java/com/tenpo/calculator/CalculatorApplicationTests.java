package com.tenpo.calculator;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tenpo.calculator.dto.ResponseDto;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class CalculatorApplicationTests {
    protected MockMvc mvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Before()
    public void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }


    private <T> T mapFromJson(String json) throws Exception {
        return objectMapper.readValue(json, (Class<T>) ResponseDto.class);
    }
    

    private MockHttpServletResponse makeCallToService(String uri) throws Exception {
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        return mvcResult.getResponse();
    }

    @Test
    public void getAdd() throws Exception {
        MockHttpServletResponse httpResponse = makeCallToService("/add?firstNumber=2343&secondNumber=4");

        assertEquals(200, httpResponse.getStatus());

        ResponseDto result = mapFromJson(httpResponse.getContentAsString());
        assertEquals(result.getResult(), BigDecimal.valueOf(2347));
    }

    @Test
    public void getBadRequest() throws Exception {
        MockHttpServletResponse httpResponse = makeCallToService("/add?firstNumber=2343&secondNumber=fsd");

        assertEquals(400, httpResponse.getStatus());
    }

}

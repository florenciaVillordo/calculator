package com.tenpo.calculator.security;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tenpo.calculator.controller.CalculatorConstant;
import com.tenpo.calculator.security.dto.SingUpDto;
import com.tenpo.calculator.security.dto.UserDto;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SecurityTest {
    @Autowired
    private WebApplicationContext context;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private MockMvc mockMvc;

    private UserDto validUser;
    private UserDto invalidUser;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        validUser = getCorrectUser();
        invalidUser = getWrongUser();
    }

    @Test
    public void signup_shouldSucceed() throws Exception {
        SingUpDto request = SingUpDto.builder()
                .username("foo")
                .password("pass")
                .confirmPassword("pass").build();

        mockMvc.perform(MockMvcRequestBuilders.post(CalculatorConstant.AUTH_SIGN_UP_URI).content(mapToJson(request)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void signup_shouldFail() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(CalculatorConstant.AUTH_SIGN_UP_URI).content(mapToJson(getSingUpDto())).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void login_shouldSucceed() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post(CalculatorConstant.AUTH_LOGIN_URI).content(mapToJson(validUser)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void login_shouldFail() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post(CalculatorConstant.AUTH_LOGIN_URI).content(mapToJson(invalidUser)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenAuthRequest_shouldSucceed() throws Exception {

        String token =
                mockMvc.perform(MockMvcRequestBuilders.post(CalculatorConstant.AUTH_LOGIN_URI).content(mapToJson(validUser)).contentType(MediaType.APPLICATION_JSON))
                        .andReturn().getResponse().getContentAsString();

        mockMvc.perform(MockMvcRequestBuilders.get("/add?firstNumber=4&secondNumber=5").header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void givenAuthRequestWithoutUser_shouldFail() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/add?firstNumber=4&secondNumber=5").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    public void givenLogoutUser_shouldFail() throws Exception {
        String token =
                mockMvc.perform(MockMvcRequestBuilders.post(CalculatorConstant.AUTH_LOGIN_URI).content(mapToJson(validUser)).contentType(MediaType.APPLICATION_JSON))
                        .andReturn().getResponse().getContentAsString();

        mockMvc.perform(MockMvcRequestBuilders.get(CalculatorConstant.AUTH_LOGOUT_URI).header("Authorization", token).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.get("/add?firstNumber=4&secondNumber=5").header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    private String mapToJson(Object obj) throws Exception {
        return objectMapper.writeValueAsString(obj);
    }

    private static SingUpDto getSingUpDto(){
        return SingUpDto.builder().username("name").password("validpass")
                .confirmPassword("validpass")
                .build();
    }

    private static UserDto getCorrectUser() {
        return UserDto.builder().username("name").password("validpass")
                .build();
    }

    private static UserDto getWrongUser() {
        return UserDto.builder().username("name").password("passFailed")
                .build();
    }
}

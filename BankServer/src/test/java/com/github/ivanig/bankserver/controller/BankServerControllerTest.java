package com.github.ivanig.bankserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ivanig.common.messages.PinCodeStatus;
import com.github.ivanig.common.messages.RequestFromAtm;
import com.github.ivanig.common.messages.ResponseToAtm;
import lombok.SneakyThrows;
import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BankServerControllerTest {

    @Autowired
    private MockMvc mvc;

    private static String headerValue;
    private static final ObjectMapper MAPPER = new ObjectMapper();

    @BeforeAll
    public static void setUp(@Value("${securityConfig.inMemUsername}") String user,
                             @Value("${securityConfig.inMemPassword}") String pass) {
        String auth = user + ":" + pass;
        byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(StandardCharsets.US_ASCII));
        headerValue = "Basic " + new String(encodedAuth);
    }

    @SneakyThrows
    @Test
    public void successGetClientInfo() {
        RequestFromAtm request = new RequestFromAtm(
                "ANDREY", "VASILIEV", 1616161616161111L,1234);

        Map<String, String> balances = new HashMap<String, String>() {{
            put("77777777777777733333", "0.00 EUR");
            put("77777777777777722222", "0.00 USD");
            put("77777777777777711111", "0.00 RUB");
        }};
        ResponseToAtm expectedResponse = new ResponseToAtm(
                "ANDREY", "IVANOVICH", balances, PinCodeStatus.OK);

        mvc.perform(post("/clientInfo").contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, headerValue)
                        .content(MAPPER.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string(MAPPER.writeValueAsString(expectedResponse)));
    }

    @SneakyThrows
    @Test
    public void successGetClientInfo_invalidPinCode() {
        RequestFromAtm request = new RequestFromAtm(
                "ANDREY", "VASILIEV", 1616161616161111L,0);

        ResponseToAtm expectedResponse = new ResponseToAtm(
                "ANDREY", "IVANOVICH", Collections.emptyMap(), PinCodeStatus.INVALID);

        mvc.perform(post("/clientInfo").contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, headerValue)
                        .content(MAPPER.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string(MAPPER.writeValueAsString(expectedResponse)));
    }

    @SneakyThrows
    @Test
    public void failGetClientInfo_404CodeDueToInvalidClientName() {
        RequestFromAtm request = new RequestFromAtm(
                "BOBA", "FETT", 1616161616162222L, 1234);

        mvc.perform(post("/clientInfo").contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, headerValue)
                        .content(MAPPER.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @SneakyThrows
    @Test
    public void failGetClientInfo_404CodeDueToInvalidCardNumber() {
        RequestFromAtm request = new RequestFromAtm(
                "AFANASII", "FET", 99L, 1234);

        mvc.perform(post("/clientInfo").contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, headerValue)
                        .content(MAPPER.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }
}
package com.github.ivanig.atmserver.service;

import com.github.ivanig.atmserver.dto.ResponseToClient;
import com.github.ivanig.common.messages.PinCodeStatus;
import com.github.ivanig.common.messages.ResponseToAtm;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class AtmServiceTest {

    @Value("${atmService.auth.username}")
    private String serverLogin;

    @Value("${atmService.auth.password}")
    private String serverPassword;

    @Autowired
    private AtmService atmService;

    @Test
    void successAnalyzeAndConvertToResponseForClient_OkPinCode() {
        Map<String, String> accountsAndBalances = new HashMap<String, String>() {{
            put("20", "0.00 EUR");
            put("21", "0.00 RUB");
        }};

        ResponseToAtm responseFromBank = new ResponseToAtm(
                "fn", "pa", accountsAndBalances, PinCodeStatus.OK);

        ResponseToClient actual = atmService.analyzeAndConvertToResponseForClient(responseFromBank);

        ResponseToClient expected = new ResponseToClient(
                "fn pa", accountsAndBalances, "Ok");

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void successAnalyzeAndConvertToResponseForClient_invalidPinCode() {
        ResponseToAtm responseFromBank = new ResponseToAtm(
                "fn", "pa", new HashMap<>(), PinCodeStatus.INVALID);

        ResponseToClient actual = atmService.analyzeAndConvertToResponseForClient(responseFromBank);

        ResponseToClient expected = new ResponseToClient(
                "", Collections.emptyMap(), "Invalid pin-code entered");

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void successCreateAuthHeaderValue() {
        String auth = serverLogin + ":" + serverPassword;

        String actual = "Basic " + new String(
                Base64.getMimeEncoder().encode(auth.getBytes(StandardCharsets.UTF_8)));

        String expected = atmService.createAuthHeaderValue();

        Assertions.assertEquals(expected, actual);
    }
}
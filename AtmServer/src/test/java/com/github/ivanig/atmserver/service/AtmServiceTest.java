package com.github.ivanig.atmserver.service;

import com.github.ivanig.atmserver.dto.ResponseToClient;
import com.github.ivanig.common.messages.ResponseToAtm;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AtmServiceTest {

    static AtmService atmService;

    @BeforeAll
    static void setUp() {
        atmService = new AtmService();
    }

    @Test
    public void checkResponsePreparing() {

    Map<String, String> accountInfo = new HashMap<>();
    accountInfo.put("77777777777777711111", "0.00 RUB");
    ResponseToAtm responseFromBank = new ResponseToAtm(accountInfo);

    ResponseToClient expectedResponse = new ResponseToClient(accountInfo);

    assertEquals(expectedResponse, atmService.prepareResponseToClient(responseFromBank));

    }
}
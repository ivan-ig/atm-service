package com.github.ivanig.bankserver.controller;

import com.github.ivanig.bankserver.exceptions.ClientNotFoundException;
import com.github.ivanig.bankserver.repository.BankRepository;
import com.github.ivanig.bankserver.service.BankService;
import com.github.ivanig.common.messages.RequestFromAtm;
import com.github.ivanig.common.messages.ResponseToAtm;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BankServerControllerTest {

    static BankRepository bankRepository;
    static BankServerController bankServerController;
    static BankService bankService;

    @BeforeAll
    static void setUp() {

        bankRepository = new BankRepository();

        bankService = new BankService(bankRepository);

        bankServerController = new BankServerController(bankService);
    }

    @Test
    public void getSingleAccountInfo() {

        Map<String, String> accounts = new HashMap<>();
        String roubles = "0.00 RUB";
        accounts.put("88888888888888811111", roubles);

        ResponseToAtm expectedResponse = new ResponseToAtm(accounts);

        ResponseToAtm response = bankServerController.getCardAccountsInfo(
                new RequestFromAtm("AFANASII", "FET", 1616161616162222L, 1234));

        assertEquals(expectedResponse, response);
    }

    @Test
    public void getMultipleAccountInfo() {

        Map<String, String> accounts = new HashMap<>();
        String roubles = "0.00 RUB";
        String dollars = "0.00 USD";
        accounts.put("77777777777777711111", roubles);
        accounts.put("77777777777777722222", dollars);

        ResponseToAtm expectedResponse = new ResponseToAtm(accounts);

        ResponseToAtm response = bankServerController.getCardAccountsInfo(
                new RequestFromAtm("ANDREY", "VASILIEV", 1616161616161111L, 1234));

        assertEquals(expectedResponse, response);
    }

    @Test
    public void failedResponseDueToIncorrectRequestParam() {

        RequestFromAtm incorrectRequest = new RequestFromAtm("", "FETT", 11L, 1234);

        assertThrows(ClientNotFoundException.class, () -> bankServerController.getCardAccountsInfo(incorrectRequest));
    }
}
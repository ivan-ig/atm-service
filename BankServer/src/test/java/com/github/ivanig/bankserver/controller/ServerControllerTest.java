package com.github.ivanig.bankserver.controller;

import com.github.ivanig.bankserver.repository.AccountRepository;
import com.github.ivanig.bankserver.repository.AccountRepositoryImpl;
import com.github.ivanig.bankserver.repository.dao.H2DatabaseAccessor;
import com.github.ivanig.bankserver.repository.dao.H2DatabaseAccessorImpl;
import com.github.ivanig.bankserver.service.AccountService;
import com.github.ivanig.bankserver.service.AccountServiceImpl;
import com.github.ivanig.common.dto.RequestFromAtm;
import com.github.ivanig.common.dto.ResponseToAtm;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ServerControllerTest {

    static H2DatabaseAccessor h2DatabaseAccessor;
    static AccountRepository accountRepository;
    static ServerController serverController;
    static AccountService accountService;

    @BeforeAll
    static void setUp() {

        h2DatabaseAccessor = new H2DatabaseAccessorImpl();

        accountRepository = new AccountRepositoryImpl(h2DatabaseAccessor);

        accountService = new AccountServiceImpl(accountRepository);

        serverController = new ServerController(accountService);
    }

    @Test
    public void getSingleAccountInfo() {

        Map<String, String> accounts = new HashMap<>();
        String roubles = "0.00 RUB";
        accounts.put("88888888888888811111", roubles);

        ResponseToAtm expectedResponse = new ResponseToAtm(accounts);

        ResponseToAtm response = serverController.getCardInfo(new RequestFromAtm("AFANASII", "FET", 1616161616162222L, 1234));

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

        ResponseToAtm response = serverController.getCardInfo(new RequestFromAtm("ANDREY", "VASILIEV", 1616161616161111L, 1234));

        assertEquals(expectedResponse, response);
    }

    @Test
    public void failedResponseDueToIncorrectRequestParam() {
        Map<String, String> accounts = new HashMap<>();

        ResponseToAtm expectedResponse = new ResponseToAtm(accounts);

        ResponseToAtm response = serverController.getCardInfo(new RequestFromAtm("PINEAPPLE", "FETT", 11L, 1234));

        assertEquals(expectedResponse, response);
    }
}
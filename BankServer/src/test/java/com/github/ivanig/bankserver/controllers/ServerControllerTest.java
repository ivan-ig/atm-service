package com.github.ivanig.bankserver.controllers;

import com.github.ivanig.bankserver.domain.Account;
import com.github.ivanig.bankserver.dto.RequestFromAtm;
import com.github.ivanig.bankserver.dto.ResponseToAtm;
import com.github.ivanig.bankserver.repository.AccountRepository;
import com.github.ivanig.bankserver.repository.AccountRepositoryImpl;
import com.github.ivanig.bankserver.repository.dao.H2DatabaseAccessor;
import com.github.ivanig.bankserver.repository.dao.H2DatabaseAccessorImpl;
import com.github.ivanig.bankserver.service.AccountService;
import com.github.ivanig.bankserver.service.AccountServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

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

        Set<Account> accounts = new HashSet<Account>() {{
            add(new Account("88888888888888811111",1616161616162222L, "RUB", new BigDecimal("0.00")));
        }};
        ResponseToAtm expectedResponse = new ResponseToAtm(accounts);

        ResponseToAtm response = serverController.getCardInfo(new RequestFromAtm("AFANASII", "FET", 1616161616162222L));

        Assertions.assertEquals(expectedResponse, response);
    }

    @Test
    public void getMultipleAccountInfo() {
        Set<Account> accounts = new HashSet<Account>() {{
            add(new Account("77777777777777711111",1616161616161111L, "RUB", new BigDecimal("0.00")));
            add(new Account("77777777777777722222",1616161616161111L, "USD", new BigDecimal("0.00")));
            add(new Account("77777777777777733333",1616161616161111L, "EUR", new BigDecimal("0.00")));
        }};
        ResponseToAtm expectedResponse = new ResponseToAtm(accounts);

        ResponseToAtm response = serverController.getCardInfo(new RequestFromAtm("ANDREY", "VASILIEV", 1616161616161111L));

        Assertions.assertEquals(expectedResponse, response);
    }

    //TODO
    // тест аккаунта без карт-счета (с пустым ответом)
    // тест с неправильными входными данными
    // тесты внутренней логики классов доменной модели


}
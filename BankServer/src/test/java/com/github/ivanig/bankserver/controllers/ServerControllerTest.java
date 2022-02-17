package com.github.ivanig.bankserver.controllers;

import com.github.ivanig.bankserver.domain.Account;
import com.github.ivanig.bankserver.domain.BankClient;
import com.github.ivanig.bankserver.dto.RequestFromAtm;
import com.github.ivanig.bankserver.dto.ResponseToAtm;
import com.github.ivanig.bankserver.repository.AccountRepository;
import com.github.ivanig.bankserver.repository.AccountRepositoryImpl;
import com.github.ivanig.bankserver.service.AccountService;
import com.github.ivanig.bankserver.service.AccountServiceImpl;
import com.github.ivanig.bankserver.utils.JSONUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

class ServerControllerTest {

    static AccountRepository accountRepository;

    static ServerController serverController;

    static AccountService accountService;

    @BeforeAll
    static void setUp() {

        Set<BankClient> dataBase = new HashSet<>();

        Set<Account> dataBaseAccounts1 = new HashSet<>();
        dataBaseAccounts1.add(new Account("0000",
                4444L, "RUB", new BigDecimal("10000.00")));
        dataBaseAccounts1.add(new Account("1111",
                4444L, "USD", new BigDecimal("5000.00")));
        dataBaseAccounts1.add(new Account("2222",
                4444L, "EUR", new BigDecimal("2000.00")));
        dataBaseAccounts1.add(new Account("3333",
                -1L, "RUB", new BigDecimal("100000.00")));

        Set<Account> dataBaseAccounts2 = new HashSet<>();
        dataBaseAccounts2.add(new Account("4444",
                5555L, "RUB", new BigDecimal("40000.00")));
        dataBaseAccounts2.add(new Account("5555",
                -1L, "USD", new BigDecimal("1000.00")));

        dataBase.add(new BankClient(1, "ivan", "1", dataBaseAccounts1));
        dataBase.add(new BankClient(2, "ivan", "2", dataBaseAccounts2));

        accountRepository = new AccountRepositoryImpl(dataBase);

        accountService = new AccountServiceImpl(accountRepository);

        serverController = new ServerController(accountService);
    }

    @Test
    public void getCardInfo() {

        Set<Account> accounts = new HashSet<Account>() {{
            add(new Account("4444",5555L, "RUB", new BigDecimal("40000.00")));
        }};
        ResponseToAtm expectedResponse = new ResponseToAtm(accounts);

        ResponseToAtm response = serverController.getCardInfo(new RequestFromAtm("ivan", "2", 5555L));

        Assertions.assertEquals(JSONUtils.toJSON(expectedResponse), JSONUtils.toJSON(response));
    }

    //TODO
    // тест с несколькими счетами в ответе
    // тест аккаунта без карт-счета (с пустым ответом)
    // тест с неправильными входными данными
    // тесты внутренней логики классов доменной модели


}
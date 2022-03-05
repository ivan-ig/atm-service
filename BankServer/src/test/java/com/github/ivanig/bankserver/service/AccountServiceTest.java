package com.github.ivanig.bankserver.service;

import com.github.ivanig.bankserver.model.Account;
import com.github.ivanig.bankserver.model.BankClient;
import com.github.ivanig.bankserver.repository.BankRepository;
import com.github.ivanig.common.messages.RequestFromAtm;
import com.github.ivanig.common.messages.ResponseToAtm;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

class AccountServiceTest {

    @Test
    public void getResponse() {
        BankRepository bankRepository = Mockito.mock(BankRepository.class);

        Set<Account> accounts = new HashSet<Account>(){{
            add(new Account("20", null, "RUB", new BigDecimal("0.00")));
            add(new Account("21", 16L, "RUB", new BigDecimal("0.00")));
            add(new Account("22", 16L, "USD", new BigDecimal("0.00")));
        }};

        RequestFromAtm request = new RequestFromAtm("1", "2", 16L, 1234);

        when(bankRepository.getClientFromRepository(request)).then(e ->
                Optional.of(new BankClient(anyString(), anyString(), accounts)));

        AccountService accountService = new AccountService(bankRepository);

        Map<String, String> map = new HashMap<String, String>() {{
            put("21", "0.00 RUB");
            put("22", "0.00 USD");
        }};
        ResponseToAtm expectedResponse = new ResponseToAtm(map);

        Assertions.assertEquals(expectedResponse, accountService.prepareResponse(request));
    }
}
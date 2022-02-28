package com.github.ivanig.atmserver.controller;

import com.github.ivanig.atmserver.AtmService;
import com.github.ivanig.atmserver.AtmServiceImpl;
import com.github.ivanig.atmserver.dto.ResponseToClient;
import com.github.ivanig.bankserver.repository.AccountRepositoryImpl;
import com.github.ivanig.bankserver.repository.dao.H2DatabaseAccessorImpl;
import com.github.ivanig.bankserver.service.AccountServiceImpl;
import com.github.ivanig.common.dto.ResponseToAtm;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.when;

class ServerControllerTest {

    static com.github.ivanig.bankserver.controller.ServerController bankController;
    static AtmService atmService;
    static ServerController atmController;

    @Test
    public void successfulGettingBalance_integrationTesting() {
        atmService = new AtmServiceImpl();
        bankController = new com.github.ivanig.bankserver.controller.ServerController(
                new AccountServiceImpl(new AccountRepositoryImpl(new H2DatabaseAccessorImpl())));
        atmController = new ServerController(bankController, atmService);

        String firstName = "ANDREY";
        String lastName = "VASILIEV";
        long cardNumber = 1616161616161111L;
        int PIN = 1234;

        Map<String, String> accountInfo = new HashMap<>();
        accountInfo.put("77777777777777711111", "0.00 RUB");
        accountInfo.put("77777777777777722222", "0.00 USD");
        ResponseToClient expectedResponse = new ResponseToClient(accountInfo);

        Assertions.assertEquals(expectedResponse,
                atmController.getBalance(firstName, lastName, cardNumber, PIN));
    }

    @Test
    public void successfulGettingBalance_unitTesting() {

        Map<String, String> accountInfo = new HashMap<>();
        accountInfo.put("11111", "0.00 RUB");
        accountInfo.put("22222", "0.00 RUB");
        ResponseToAtm response = new ResponseToAtm(accountInfo);

        atmService = new AtmServiceImpl();
        bankController = Mockito.mock(com.github.ivanig.bankserver.controller.ServerController.class);
        atmController = new ServerController(bankController, atmService);

        when(bankController.getCardInfo(Mockito.any())).then(e -> response);

        Assertions.assertEquals(2, atmController.getBalance(
                "f", "l", 11L, 1234).getAccountView().size());
    }
}
package com.github.ivanig.atmserver.controller;

import com.github.ivanig.atmserver.dto.ResponseToClient;
import com.github.ivanig.atmserver.service.AtmService;
import com.github.ivanig.bankserver.controller.BankServerController;
import com.github.ivanig.bankserver.repository.BankRepository;
import com.github.ivanig.bankserver.service.BankService;
import com.github.ivanig.common.messages.ResponseToAtm;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.when;

class AtmServerControllerTest {

    static BankServerController bankController;
    static AtmService atmService;
    static AtmServerController atmController;

    @Test
    public void successfulGettingBalance_integrationTesting() {
        atmService = new AtmService();
        bankController = new BankServerController(
                new BankService(new BankRepository()));
        atmController = new AtmServerController(bankController, atmService);

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

        atmService = new AtmService();
        bankController = Mockito.mock(BankServerController.class);
        atmController = new AtmServerController(bankController, atmService);

        when(bankController.getCardAccountsInfo(Mockito.any())).then(e -> response);

        int expectedResponseToClientSize = 2;

        Assertions.assertEquals(expectedResponseToClientSize, atmController.getBalance(
                "f", "l", 11L, 1234).getAccountView().size());
    }
}
package com.github.ivanig.atmserver.controller;

import com.github.ivanig.atmserver.dto.ResponseToClient;
import com.github.ivanig.atmserver.service.AtmService;
import com.github.ivanig.bankserver.controller.BankServerController;
import com.github.ivanig.common.messages.RequestFromAtm;
import com.github.ivanig.common.messages.ResponseToAtm;
import lombok.Data;
import lombok.NonNull;

@Data
public class AtmServerController {

    @NonNull
    private BankServerController bankController;
    @NonNull
    private AtmService atmService;

    public ResponseToClient getBalance(String firstName,
                                       String lastName,
                                       long cardNumber,
                                       int PIN) {

        RequestFromAtm request = new RequestFromAtm(firstName, lastName, cardNumber, PIN);
        ResponseToAtm responseFromBank = restTemplate(request);

        return atmService.getResponseToClient(responseFromBank);
    }

    private ResponseToAtm restTemplate(RequestFromAtm request) {
        return bankController.getCardAccountsInfo(request);
    }
}
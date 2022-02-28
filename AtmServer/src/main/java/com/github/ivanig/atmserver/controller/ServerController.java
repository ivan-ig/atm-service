package com.github.ivanig.atmserver.controller;

import com.github.ivanig.atmserver.AtmService;
import com.github.ivanig.atmserver.dto.ResponseToClient;
import com.github.ivanig.common.dto.RequestFromAtm;
import com.github.ivanig.common.dto.ResponseToAtm;
import lombok.Data;
import lombok.NonNull;

@Data
public class ServerController {

    @NonNull
    private com.github.ivanig.bankserver.controller.ServerController bankController;
    @NonNull
    private AtmService atmService;

    public ResponseToClient getBalance(String firstName,
                                       String lastName,
                                       long cardNumber,
                                       int PIN) {

        RequestFromAtm request = new RequestFromAtm(firstName, lastName, cardNumber, PIN);
        ResponseToAtm responseFromBank = restTemplate(request);

        return atmService.prepareResponseToClient(responseFromBank);
    }

    private ResponseToAtm restTemplate(RequestFromAtm request) {
        return bankController.getCardInfo(request);
    }
}
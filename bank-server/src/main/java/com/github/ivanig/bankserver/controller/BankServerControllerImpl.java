package com.github.ivanig.bankserver.controller;

import com.github.ivanig.bankserver.service.BankService;
import com.github.ivanig.common.messages.RequestFromAtm;
import com.github.ivanig.common.messages.ResponseToAtm;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class BankServerControllerImpl implements BankServerController {

    private BankService bankService;

    @Override
    public ResponseToAtm getClientInfo(RequestFromAtm request) {
        return bankService.getCardAccountsInfoAndConvertToResponse(request);
    }
}

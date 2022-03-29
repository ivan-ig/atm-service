package com.github.ivanig.bankserver.controller;

import com.github.ivanig.bankserver.service.BankService;
import com.github.ivanig.common.messages.RequestFromAtm;
import com.github.ivanig.common.messages.ResponseToAtm;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class BankServerControllerImpl implements BankServerController {

    private BankService bankService;

    @Override
    public ResponseToAtm getClientInfo(RequestFromAtm request) {
        log.info("BankServerController: " + request);
        return bankService.getCardAccountsInfoAndConvertToResponse(request);
    }
}

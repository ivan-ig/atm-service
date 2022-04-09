package com.github.ivanig.bankserver.rest.controller;

import com.github.ivanig.bankserver.service.BankServerService;
import com.github.ivanig.common.messages.RequestFromAtm;
import com.github.ivanig.common.messages.ResponseToAtm;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class BankServerControllerImpl implements BankServerController {

    private BankServerService bankServerService;

    @Override
    public ResponseToAtm getClientInfo(RequestFromAtm request) {
        log.debug("Received(REST): [" + request + "]");
        return bankServerService.getCardAccountsInfoAndConvertToResponse(request);
    }
}

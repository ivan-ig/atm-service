package com.github.ivanig.bankserver.rest.controller;

import com.github.ivanig.bankserver.service.BankServerService;
import com.github.ivanig.common.messages.RequestFromAtm;
import com.github.ivanig.common.messages.ResponseToAtm;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Component
@AllArgsConstructor
public class BankServerControllerImpl implements BankServerController {

    private BankServerService bankServerService;

    @Override
    @PostMapping(path = "/clientInfo",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseToAtm getClientInfo(RequestFromAtm request) {
        log.debug("Received(REST): [" + request + "]");
        return bankServerService.getCardAccountsInfoAndConvertToResponse(request);
    }
}

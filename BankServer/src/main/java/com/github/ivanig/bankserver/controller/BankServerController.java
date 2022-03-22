package com.github.ivanig.bankserver.controller;

import com.github.ivanig.common.messages.RequestFromAtm;
import com.github.ivanig.common.messages.ResponseToAtm;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController("/")
public interface BankServerController {

    @PostMapping(path = "/clientInfo",
                 consumes = MediaType.APPLICATION_JSON_VALUE,
                 produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseToAtm getCardAccountsInfoAndConvertToResponse(@RequestBody RequestFromAtm request);

}
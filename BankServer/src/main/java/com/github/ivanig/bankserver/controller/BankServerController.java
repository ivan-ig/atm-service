package com.github.ivanig.bankserver.controller;

import com.github.ivanig.common.messages.RequestFromAtm;
import com.github.ivanig.common.messages.ResponseToAtm;
import org.springframework.web.bind.annotation.*;

@RestController("/")
public interface BankServerController {

    @PostMapping(path = "clientInfo", consumes = "application/json", produces = "application/json")
    ResponseToAtm getCardAccountsInfoAndConvertToResponse(@RequestBody RequestFromAtm request);

}
package com.github.ivanig.bankserver.rest.controller;

import com.github.ivanig.common.messages.RequestFromAtm;
import com.github.ivanig.common.messages.ResponseToAtm;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public interface BankServerController {

    ResponseToAtm getClientInfo(@RequestBody RequestFromAtm request);

}
package com.github.ivanig.bankserver.controllers;

import com.github.ivanig.bankserver.dto.RequestFromAtm;
import com.github.ivanig.bankserver.dto.ResponseToAtm;
import com.github.ivanig.bankserver.service.AccountService;
import lombok.Data;
import lombok.NonNull;

public @Data class ServerController {

    @NonNull
    private AccountService accountService;

    public ResponseToAtm getCardInfo(RequestFromAtm request) {
        return accountService.getResponse(request);
    }
}

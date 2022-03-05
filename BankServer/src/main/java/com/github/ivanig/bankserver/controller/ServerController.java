package com.github.ivanig.bankserver.controller;

import com.github.ivanig.bankserver.service.AccountService;
import com.github.ivanig.common.dto.RequestFromAtm;
import com.github.ivanig.common.dto.ResponseToAtm;
import lombok.Data;
import lombok.NonNull;

public @Data class ServerController {

    @NonNull
    private AccountService accountService;

    public ResponseToAtm getCardAccountsInfo(RequestFromAtm request) {
        return accountService.prepareResponse(request);
    }
}

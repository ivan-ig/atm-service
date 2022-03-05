package com.github.ivanig.bankserver.controller;

import com.github.ivanig.bankserver.service.BankService;
import com.github.ivanig.common.messages.RequestFromAtm;
import com.github.ivanig.common.messages.ResponseToAtm;
import lombok.Data;
import lombok.NonNull;

@Data
public class BankServerController {

    @NonNull
    private BankService bankService;

    public ResponseToAtm getCardAccountsInfo(RequestFromAtm request) {
        return bankService.prepareResponse(request);
    }
}

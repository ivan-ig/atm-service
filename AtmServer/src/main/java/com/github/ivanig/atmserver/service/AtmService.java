package com.github.ivanig.atmserver.service;

import com.github.ivanig.atmserver.dto.ResponseToClient;
import com.github.ivanig.common.messages.ResponseToAtm;

public class AtmService {

    public ResponseToClient prepareResponseToClient(ResponseToAtm responseFromBank) {

        return new ResponseToClient(responseFromBank.getCardAccounts());
    }
}

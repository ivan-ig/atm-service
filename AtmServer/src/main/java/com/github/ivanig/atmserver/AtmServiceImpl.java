package com.github.ivanig.atmserver;

import com.github.ivanig.atmserver.dto.ResponseToClient;
import com.github.ivanig.common.dto.ResponseToAtm;

public class AtmServiceImpl implements AtmService {

    @Override
    public ResponseToClient prepareResponseToClient(ResponseToAtm responseFromBank) {

        return new ResponseToClient(responseFromBank.getCardAccounts());
    }
}

package com.github.ivanig.atmserver.service;

import com.github.ivanig.atmserver.dto.ResponseToClient;
import com.github.ivanig.common.messages.ResponseToAtm;
import org.springframework.stereotype.Service;

@Service
public class AtmService {

    public ResponseToClient getResponseToClient(ResponseToAtm responseFromBank) {

        String clientName = responseFromBank.getFirstname() + " " + responseFromBank.getPatronymic();

        return new ResponseToClient(clientName, responseFromBank.getAccountsAndBalances());
    }
}

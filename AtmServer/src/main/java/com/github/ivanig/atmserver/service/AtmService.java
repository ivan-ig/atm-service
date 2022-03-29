package com.github.ivanig.atmserver.service;

import com.github.ivanig.atmserver.dto.ResponseToClient;
import com.github.ivanig.common.messages.PinCodeStatus;
import com.github.ivanig.common.messages.ResponseToAtm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class AtmService {

    @Value("${atmService.auth.username}")
    private String serverLogin;

    @Value("${atmService.auth.password}")
    private String serverPassword;

    public ResponseToClient analyzeAndConvertToResponseForClient(ResponseToAtm responseFromBank) {
        String clientName = responseFromBank.getFirstname() + " " + responseFromBank.getPatronymic();
        Map<String, String> accountsAndBalances =
                Collections.unmodifiableMap(responseFromBank.getAccountsAndBalances());
        String pinCodeStatus = "Ok";

        if (responseFromBank.getPinCodeStatus() == PinCodeStatus.INVALID) {
            clientName = "";
            accountsAndBalances = Collections.unmodifiableMap(new HashMap<>());
            pinCodeStatus = "Invalid pin-code entered.";
        }

        return new ResponseToClient(clientName, accountsAndBalances, pinCodeStatus);
    }

    public String createAuthHeaderValue() {
        String auth = serverLogin + ":" + serverPassword;
        byte[] encodedAuth = Base64.getMimeEncoder().encode(auth.getBytes(StandardCharsets.UTF_8));
        return "Basic " + new String(encodedAuth);
    }
}

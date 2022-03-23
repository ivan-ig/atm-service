package com.github.ivanig.atmserver.controller;

import com.github.ivanig.atmserver.dto.ResponseToClient;
import com.github.ivanig.atmserver.exceptions.InternalBankServerErrorException;
import com.github.ivanig.atmserver.service.AtmService;
import com.github.ivanig.common.messages.RequestFromAtm;
import com.github.ivanig.common.messages.ResponseToAtm;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class AtmServerControllerImpl implements AtmServerController {

    private AtmService atmService;

    @Override
    public ResponseToClient getBalance(@Value("${cardData.firstname}") String firstName,
                                       @Value("${cardData.lastname}") String lastName,
                                       @Value("${cardData.number}") long cardNumber,
                                       @Value("${cardData.pinCode}") int pinCode) {

        RequestFromAtm request = new RequestFromAtm(firstName, lastName, cardNumber, pinCode);
        log.info(request.toString());

        ResponseToAtm responseFromBank = atmService.getClientInfoFromBank(request)
                .blockOptional().orElseThrow(() -> {
                    log.info("InternalBankServerErrorException: Server returns \"null\".");
                    return new InternalBankServerErrorException();
                });

        return atmService.analyzeAndConvertToResponseForClient(responseFromBank);
    }
}

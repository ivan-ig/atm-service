package com.github.ivanig.atmserver.service;

import com.github.ivanig.atmserver.controller.AtmServerController;
import com.github.ivanig.atmserver.dto.ResponseToClient;
import com.github.ivanig.atmserver.exceptions.BadRequestException;
import com.github.ivanig.atmserver.exceptions.InternalBankServerErrorException;
import com.github.ivanig.common.messages.PinCodeStatus;
import com.github.ivanig.common.messages.RequestFromAtm;
import com.github.ivanig.common.messages.ResponseToAtm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class AtmService implements AtmServerController {

    public ResponseToClient getBalance(@Value("${cardData.firstname}") String firstName,
                                       @Value("${cardData.lastname}") String lastName,
                                       @Value("${cardData.number}") long cardNumber,
                                       @Value("${cardData.pinCode}") int pinCode,
                                       @Value("${webClient.bankServerURL}") String bankServerURL) {

        RequestFromAtm request = new RequestFromAtm(firstName, lastName, cardNumber, pinCode);
        log.info(request.toString());

        ResponseToAtm response = WebClient
                .create(bankServerURL)
                .post()
                .uri("clientInfo")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(Mono.just(request), RequestFromAtm.class)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError,
                        er -> {
                            log.info("BadRequestException: Something is wrong with request body/parameters.");
                            return Mono.error(new BadRequestException());
                        })
                .onStatus(HttpStatus::is5xxServerError,
                        er -> {
                            log.info("InternalBankServerErrorException: Server is not responding.");
                            return Mono.error(new InternalBankServerErrorException());
                        })
                .bodyToMono(ResponseToAtm.class)
                .blockOptional().orElseThrow(() -> {
                    log.info("InternalBankServerErrorException: Server returns \"null\".");
                    return new InternalBankServerErrorException();
                });

        return analyzeAndConvertResponseFromBankToResponseForClient(response);
    }

    public ResponseToClient analyzeAndConvertResponseFromBankToResponseForClient(ResponseToAtm responseFromBank) {
        String clientName = responseFromBank.getFirstname() + " " + responseFromBank.getPatronymic();
        Map<String, String> accountsAndBalances =
                Collections.unmodifiableMap(responseFromBank.getAccountsAndBalances());
        String pinCodeStatus = "Ok";

        if (PinCodeStatus.INVALID == responseFromBank.getPinCodeStatus()) {
            clientName = "";
            accountsAndBalances = Collections.unmodifiableMap(new HashMap<>());
            pinCodeStatus = "Invalid pin-code was entered.";
        }

        return new ResponseToClient(clientName, accountsAndBalances, pinCodeStatus);
    }
}

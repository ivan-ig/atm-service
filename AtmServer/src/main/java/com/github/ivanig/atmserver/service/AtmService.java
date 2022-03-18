package com.github.ivanig.atmserver.service;

import com.github.ivanig.atmserver.controller.AtmServerController;
import com.github.ivanig.atmserver.dto.ResponseToClient;
import com.github.ivanig.atmserver.exceptions.InternalAtmServerErrorException;
import com.github.ivanig.atmserver.exceptions.InternalBankServerErrorException;
import com.github.ivanig.atmserver.exceptions.NotFoundOrBadRequestException;
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
                            er -> Mono.error(new NotFoundOrBadRequestException("Invalid request parameters...")))
                    .onStatus(HttpStatus::is5xxServerError,
                            er -> Mono.error(new InternalBankServerErrorException("Server is not responding.")))
                .bodyToMono(ResponseToAtm.class)
                .blockOptional().orElseThrow(() -> new InternalAtmServerErrorException("Server returns \"null\"."));

        return convertResponseFromBankToResponseForClient(response);
    }

    public ResponseToClient convertResponseFromBankToResponseForClient(ResponseToAtm responseFromBank) {

        String clientName = responseFromBank.getFirstname() + " " + responseFromBank.getPatronymic();
        Map<String, String> accountsAndBalances =
                Collections.unmodifiableMap(responseFromBank.getAccountsAndBalances());

        return new ResponseToClient(clientName, accountsAndBalances);
    }
}

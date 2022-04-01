package com.github.ivanig.atmserver.controller;

import com.github.ivanig.atmserver.dto.ResponseToClient;
import com.github.ivanig.atmserver.exceptions.InternalBankServerErrorException;
import com.github.ivanig.atmserver.exceptions.NotFoundException;
import com.github.ivanig.atmserver.exceptions.UnauthorizedException;
import com.github.ivanig.atmserver.service.AtmService;
import com.github.ivanig.common.messages.RequestFromAtm;
import com.github.ivanig.common.messages.ResponseToAtm;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@AllArgsConstructor
public class AtmControllerImpl implements AtmController {

    private WebClient webClient;
    private AtmService atmService;

    @Override
    public Mono<ResponseToClient> getInfoAndBalance(int atmNumber,
                                                    @Value("${atmService.cardData.firstname}") @NonNull String firstName,
                                                    @Value("${atmService.cardData.lastname}") @NonNull String lastName,
                                                    @Value("${atmService.cardData.number}") long cardNumber,
                                                    @Value("${atmService.cardData.pinCode}") int pinCode) {

        RequestFromAtm request = new RequestFromAtm(firstName, lastName, cardNumber, pinCode);
        log.info("Atm[" +atmNumber + "].Controller: " + request);

        return webClient
                .post()
                .uri("/clientInfo")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, atmService.createAuthHeaderValue())
                .body(Mono.just(request), RequestFromAtm.class)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(st -> st.value() == 401, er -> Mono.error(new UnauthorizedException()))
                .onStatus(st -> st.value() == 404, er -> Mono.error(new NotFoundException()))
                .onStatus(HttpStatus::is5xxServerError, er -> Mono.error(new InternalBankServerErrorException()))
                .bodyToMono(ResponseToAtm.class)
                .map(item -> atmService.analyzeAndConvertToResponseForClient(item));
    }
}

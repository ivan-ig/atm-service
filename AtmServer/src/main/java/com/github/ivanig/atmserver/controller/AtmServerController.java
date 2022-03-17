package com.github.ivanig.atmserver.controller;

import com.github.ivanig.atmserver.dto.ResponseToClient;
import com.github.ivanig.atmserver.service.AtmService;
import com.github.ivanig.common.messages.RequestFromAtm;
import com.github.ivanig.common.messages.ResponseToAtm;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@RestController("/")
@Data
public class AtmServerController {

    @NonNull
    private AtmService atmService;

    @GetMapping(path = "balance", produces = "application/json")
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
                .bodyToMono(ResponseToAtm.class)
                .blockOptional().orElseThrow(RuntimeException::new); // TODO:Handle it

        return atmService.getResponseToClient(response);
    }
}
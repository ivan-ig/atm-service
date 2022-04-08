package com.github.ivanig.atmserver.rest.controller;

import com.github.ivanig.atmserver.exceptions.InternalBankServerErrorException;
import com.github.ivanig.atmserver.exceptions.NotFoundException;
import com.github.ivanig.atmserver.exceptions.UnauthorizedException;
import com.github.ivanig.atmserver.rest.dto.ResponseToClient;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@AllArgsConstructor
public class AtmControllerForWebClient implements AtmController {

    private WebClient webClient;
    private AtmService atmService;

    @Override
    @GetMapping("/rest")
    public Map<String, String> getMethodDescriptions() {
        Map<String, String> methods = new HashMap<>();
        methods.put("/rest", "Get all methods of \"rest\"");
        methods.put("/balance", "Get client information and balances");
        return methods;
    }

    @Override
    @GetMapping(path = "/rest/balance")
    public Mono<ResponseToClient> getInfoAndBalance(@RequestParam(defaultValue = "1") String atmId,
                                                    @Value("${atmService.cardData.firstname}") @NonNull String firstName,
                                                    @Value("${atmService.cardData.lastname}") @NonNull String lastName,
                                                    @Value("${atmService.cardData.number}") long cardNumber,
                                                    @Value("${atmService.cardData.pinCode}") int pinCode) {

        String id = atmId+atmService.getOperationId();
        RequestFromAtm request = new RequestFromAtm(id, firstName, lastName, cardNumber, pinCode);
        log.debug("Processing(REST): [" + request + "]");

        return webClient
                .post()
                .uri("/clientInfo")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, atmService.createAuthHeaderValue())
                .body(Mono.just(request), RequestFromAtm.class)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(st -> st.value() == 401,
                        er -> Mono.error(new UnauthorizedException("Unable to authorize with request [" + id + "]")))
                .onStatus(st -> st.value() == 404,
                        er -> Mono.error(new NotFoundException("Unable to find client [" + request + "]")))
                .onStatus(HttpStatus::is5xxServerError,
                        er -> Mono.error(new InternalBankServerErrorException()))
                .bodyToMono(ResponseToAtm.class)
                .map(item -> atmService.analyzeAndConvertToResponseForClient(item));
    }
}
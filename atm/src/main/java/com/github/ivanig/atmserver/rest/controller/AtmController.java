package com.github.ivanig.atmserver.rest.controller;

import com.github.ivanig.atmserver.rest.dto.ResponseToClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public interface AtmController {

    @GetMapping(path = "/balance", produces = MediaType.APPLICATION_JSON_VALUE)
    Mono<ResponseToClient> getInfoAndBalance(@RequestParam(defaultValue = "0") int atmNumber,
                                             String firstName,
                                             String lastName,
                                             long cardNumber,
                                             int pinCode);

    @GetMapping(path = "/kafkaBalance")
    ResponseToClient getInfoAndBalanceWithKafka(@RequestParam(defaultValue = "100") int atmNumber,
                                    String firstName,
                                    String lastName,
                                    long cardNumber,
                                    int pinCode);

}
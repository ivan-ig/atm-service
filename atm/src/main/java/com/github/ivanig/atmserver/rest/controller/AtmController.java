package com.github.ivanig.atmserver.rest.controller;

import com.github.ivanig.atmserver.rest.dto.ResponseToClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
public interface AtmController {

    @GetMapping("/implementationPath")
    Map<String, String> getMethodDescriptions();

    @GetMapping("/implementationPath/balance")
    Mono<ResponseToClient> getInfoAndBalance(
            String atmId, String firstName, String lastName, long cardNumber, int pinCode);

}
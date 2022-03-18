package com.github.ivanig.atmserver.controller;

import com.github.ivanig.atmserver.dto.ResponseToClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/")
public interface AtmServerController {

    @GetMapping(path = "balance", produces = "application/json")
    ResponseToClient getBalance(String firstName, String lastName, long cardNumber, int pinCode, String bankServerURL);
}
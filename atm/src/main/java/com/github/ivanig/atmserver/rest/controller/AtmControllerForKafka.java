package com.github.ivanig.atmserver.rest.controller;

import com.github.ivanig.atmserver.kafka.AtmKafkaService;
import com.github.ivanig.atmserver.rest.dto.ResponseToClient;
import com.github.ivanig.atmserver.service.AtmService;
import com.github.ivanig.common.messages.RequestFromAtm;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@AllArgsConstructor
public class AtmControllerForKafka implements AtmController {

    private AtmKafkaService kafkaService;
    private AtmService atmService;

    @Override
    @GetMapping("/kafka")
    public Map<String, String> getMethodDescriptions() {
        Map<String, String> methods = new HashMap<>();
        methods.put("/kafka", "Get all methods of \"kafka\"");
        methods.put("/balance", "Get client information and balances");
        return methods;
    }

    @Override
    @GetMapping(path = "/kafka/balance")
    public Mono<ResponseToClient> getInfoAndBalance(@RequestParam(defaultValue = "101") String atmId,
                                                    @Value("${atmService.cardData.firstname}") @NonNull String firstName,
                                                    @Value("${atmService.cardData.lastname}") @NonNull String lastName,
                                                    @Value("${atmService.cardData.number}") long cardNumber,
                                                    @Value("${atmService.cardData.pinCode}") int pinCode) {

        String id = atmId+atmService.getOperationId();
        RequestFromAtm request = new RequestFromAtm(id, firstName, lastName, cardNumber, pinCode);
        log.debug("Processing(Kafka): [" + request + "]");

        kafkaService.sendMessage(request);
        ResponseToClient response = kafkaService.awaitMessage(id);

        return Mono.just(response);
    }
}
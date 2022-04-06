package com.github.ivanig.atmserver.rest.controller;

import com.github.ivanig.atmserver.exceptions.InternalBankServerErrorException;
import com.github.ivanig.atmserver.exceptions.NotFoundException;
import com.github.ivanig.atmserver.exceptions.UnauthorizedException;
import com.github.ivanig.atmserver.kafka.AtmKafkaService;
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
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@AllArgsConstructor
public class AtmControllerImpl implements AtmController {

    private WebClient webClient;
    private AtmService atmService;
    private AtmKafkaService kafkaService;

    @Override
    public Mono<ResponseToClient> getInfoAndBalance(int atmNumber,
                                                    @Value("${atmService.cardData.firstname}") @NonNull String firstName,
                                                    @Value("${atmService.cardData.lastname}") @NonNull String lastName,
                                                    @Value("${atmService.cardData.number}") long cardNumber,
                                                    @Value("${atmService.cardData.pinCode}") int pinCode) {

        RequestFromAtm request = new RequestFromAtm(firstName, lastName, cardNumber, pinCode);
        log.debug("Atm #" + atmNumber + ": [" + request + "]");

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

    @Override
    public ResponseToClient getInfoAndBalanceWithKafka(int atmNumber,
                                           @Value("${atmService.cardData.firstname}") @NonNull String firstName,
                                           @Value("${atmService.cardData.lastname}") @NonNull String lastName,
                                           @Value("${atmService.cardData.number}") long cardNumber,
                                           @Value("${atmService.cardData.pinCode}") int pinCode) {

        RequestFromAtm request = new RequestFromAtm(firstName, lastName, cardNumber, pinCode);
        log.debug("Atm #" + atmNumber + ": [" + request + "]");

        kafkaService.sendMessage(request);

        return kafkaService.awaitMessage();
    }

        //TODO
        // реализовать REST ответ от Kafka
        // Kafka и исключения на стороне сервера
        // исправить тесты
        // обновить описание в README.md
        // изучить protobuf

}

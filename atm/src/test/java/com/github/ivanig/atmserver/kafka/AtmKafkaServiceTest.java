package com.github.ivanig.atmserver.kafka;

import com.github.ivanig.atmserver.exceptions.NotFoundException;
import com.github.ivanig.atmserver.exceptions.WaitingTimeElapsedException;
import com.github.ivanig.atmserver.rest.dto.ResponseToClient;
import com.github.ivanig.atmserver.service.AtmService;
import com.github.ivanig.common.messages.RequestFromAtm;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
class AtmKafkaServiceTest {

    @Mock
    private MessageContext messageContext;

    @Mock
    private KafkaTemplate<String, RequestFromAtm> kafkaTemplate;

    private AtmKafkaService kafkaService;


    @BeforeEach
    void init() {
        kafkaService = new AtmKafkaService(kafkaTemplate, new AtmService(), messageContext);
    }


    @SneakyThrows
    @Test
    void successAwaitMessage() {

        ResponseToClient response = new ResponseToClient(
                "1:10001", "name", Collections.emptyMap(), "no matter");

        when(messageContext.getMessage("1:10001"))
                .thenReturn(Optional.of(response));

        ResponseToClient actual = kafkaService.awaitMessage("1:10001");

        assertEquals(response, actual);
    }

    @SneakyThrows
    @Test
    void failAwaitMessage_WaitingTimeElapsedException() {

        ResponseToClient response = new ResponseToClient(
                "1:10001", "name", Collections.emptyMap(), "mo matter");

        when(messageContext.getMessage(anyString()))
                .thenReturn(Optional.empty());

        assertThrows(WaitingTimeElapsedException.class, () -> kafkaService.awaitMessage("any"));
    }

    @SneakyThrows
    @Test
    void failAwaitMessage_NotFoundException() {

        ResponseToClient response = new ResponseToClient(
                "1:10001", "CLIENT NOT_FOUND", Collections.emptyMap(), "no matter");

        when(messageContext.getMessage(anyString()))
                .thenReturn(Optional.of(response));

        assertThrows(NotFoundException.class, () -> kafkaService.awaitMessage("any"));
    }
}
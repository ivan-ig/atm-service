package com.github.ivanig.atmserver.kafka;

import com.github.ivanig.atmserver.rest.dto.ResponseToClient;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class MessageContextTest {

    @Autowired
    private MessageContext messageContext;

    @SneakyThrows
    @Test
    void SuccessAddAndGetMessage() {
        ResponseToClient response = new ResponseToClient(
                "1:10001", "", Collections.emptyMap(), "Invalid pin-code entered");

        messageContext.addMessage(response);

        assertEquals(Optional.of(response), messageContext.getMessage(response.getId()));
    }

    @SneakyThrows
    @Test
    void successAddAndGetMessage_waitingTimeElapsed() {
        ResponseToClient response = new ResponseToClient(
                "1:10001", "", Collections.emptyMap(), "Invalid pin-code entered");

        messageContext.addMessage(response);

        assertEquals(Optional.empty(), messageContext.getMessage("2:20002"));
    }
}
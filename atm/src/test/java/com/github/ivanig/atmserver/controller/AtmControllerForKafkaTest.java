package com.github.ivanig.atmserver.controller;

import com.github.ivanig.atmserver.kafka.AtmKafkaService;
import com.github.ivanig.atmserver.rest.controller.AtmControllerForKafka;
import com.github.ivanig.atmserver.service.AtmService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class AtmControllerForKafkaTest {

    @Mock
    private AtmKafkaService kafkaService;

    private final  AtmControllerForKafka atmController = new AtmControllerForKafka(kafkaService, new AtmService());

    @Test
    void successGetMethodDescription() {
        Map<String, String> methods = atmController.getMethodDescriptions();

        int expectedMethodsNumber = atmController.getClass().getMethods().length - Object.class.getMethods().length;

        assertEquals(expectedMethodsNumber, methods.size());
    }
}
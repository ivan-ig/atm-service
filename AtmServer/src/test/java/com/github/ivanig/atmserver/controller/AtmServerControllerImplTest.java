package com.github.ivanig.atmserver.controller;

import com.github.ivanig.atmserver.service.AtmService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AtmServerControllerImplTest {

    private static AtmService atmService;

    @BeforeAll
    public static void setUp() {
        atmService = Mockito.mock(AtmService.class);
    }

    @Test
    void successGetBalance() {

    }

    @Test
    void successGetBalance_invalidPinCode() {

    }

    @Test
    void failGetBalance_illegalPinCodeFormat() {

    }

    @Test
    void failGetBalance_illegalCardNumberFormat() {

    }


}
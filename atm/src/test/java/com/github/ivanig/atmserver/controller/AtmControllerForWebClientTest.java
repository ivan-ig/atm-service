package com.github.ivanig.atmserver.controller;

import com.github.ivanig.atmserver.exceptions.InternalBankServerErrorException;
import com.github.ivanig.atmserver.exceptions.NotFoundException;
import com.github.ivanig.atmserver.exceptions.UnauthorizedException;
import com.github.ivanig.atmserver.rest.controller.AtmController;
import com.github.ivanig.atmserver.rest.controller.AtmControllerForWebClient;
import com.github.ivanig.atmserver.rest.dto.ResponseToClient;
import com.github.ivanig.atmserver.service.AtmService;
import lombok.SneakyThrows;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;

@SpringBootTest
class AtmControllerForWebClientTest {

    private static MockWebServer mockWebServer;
    private AtmController atmController;

    @BeforeAll
    static void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @BeforeEach
    void init() {
        WebClient webClient = WebClient.create("http://localhost:" + mockWebServer.getPort());
        atmController = new AtmControllerForWebClient(webClient, new AtmService());
    }

    @SneakyThrows
    @Test
    public void successGetInfoAndBalance() {

        mockWebServer.enqueue(
                new MockResponse()
                        .setBody("{\"firstname\":\"fn\",\"patronymic\":\"pa\"," +
                                "\"accountsAndBalances\":{\"20\":\"0.00 EUR\"," +
                                                         "\"21\":\"0.00 USD\"," +
                                                         "\"22\":\"0.00 RUB\"}," +
                                "\"pinCodeStatus\":\"OK\"}")
                        .addHeader("Content-Type", "application/json"));

        Mono<ResponseToClient> responseMock = atmController.getInfoAndBalance(
                "1", "fn", "ln", 16L, 1111);

        StepVerifier
                .create(responseMock)
                .expectNextMatches(r ->
                        (r.getClientName().contains("fn")) &&
                                !r.getAccountsView().isEmpty() &&
                                "Ok".equals(r.getPinCodeStatus()))
                .verifyComplete();

        RecordedRequest recordedRequest = mockWebServer.takeRequest();

        assertEquals("POST", recordedRequest.getMethod());
        assertEquals("/clientInfo", recordedRequest.getPath());
    }

    @SneakyThrows
    @Test
    public void successGetInfoAndBalance_invalidPinCode() {

        mockWebServer.enqueue(
                new MockResponse()
                        .setBody("{\"clientName\":{}," +
                                "\"accountsAndBalances\":{}," +
                                "\"pinCodeStatus\":\"INVALID\"}")
                        .addHeader("Content-Type", "application/json"));

        Mono<ResponseToClient> responseMock = atmController.getInfoAndBalance(
                "1", "fn", "ln", 16L, 0);

        StepVerifier
                .create(responseMock)
                .expectNextMatches(r ->
                        ("".equals(r.getClientName())) &&
                                r.getAccountsView().isEmpty() &&
                                r.getPinCodeStatus().contains("Invalid pin-code"))
                .verifyComplete();

        RecordedRequest recordedRequest = mockWebServer.takeRequest();

        assertEquals("POST", recordedRequest.getMethod());
        assertEquals("/clientInfo", recordedRequest.getPath());
    }

    @SneakyThrows
    @Test
    public void failGetInfoAndBalance_notFoundException() {

        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(404)
                        .addHeader("Content-Type", "application/json"));

        Mono<ResponseToClient> responseMock = atmController.getInfoAndBalance(
                "1", "wrong", "wrong", 0L, 0);

        StepVerifier
                .create(responseMock)
                .expectError(NotFoundException.class)
                .verify();

        RecordedRequest recordedRequest = mockWebServer.takeRequest();

        assertEquals("POST", recordedRequest.getMethod());
        assertEquals("/clientInfo", recordedRequest.getPath());
    }

    @SneakyThrows
    @Test
    public void failGetInfoAndBalance_UnauthorizedException() {

        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(401)
                        .addHeader("Content-Type", "application/json"));

        Mono<ResponseToClient> responseMock =
                atmController.getInfoAndBalance(anyString(), anyString(), anyString(), anyLong(), anyInt());

        StepVerifier
                .create(responseMock)
                .expectError(UnauthorizedException.class)
                .verify();

        RecordedRequest recordedRequest = mockWebServer.takeRequest();

        assertEquals("POST", recordedRequest.getMethod());
        assertEquals("/clientInfo", recordedRequest.getPath());
    }

    @SneakyThrows
    @Test
    public void failGetInfoAndBalance_InternalServerError() {

        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(500)
                        .addHeader("Content-Type", "application/json"));

        Mono<ResponseToClient> responseMock =
                atmController.getInfoAndBalance(anyString(), anyString(), anyString(), anyLong(), anyInt());

        StepVerifier
                .create(responseMock)
                .expectError(InternalBankServerErrorException.class)
                .verify();

        RecordedRequest recordedRequest = mockWebServer.takeRequest();

        assertEquals("POST", recordedRequest.getMethod());
        assertEquals("/clientInfo", recordedRequest.getPath());
    }
}
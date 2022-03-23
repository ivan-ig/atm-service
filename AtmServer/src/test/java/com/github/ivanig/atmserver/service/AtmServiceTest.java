package com.github.ivanig.atmserver.service;

import com.github.ivanig.common.messages.RequestFromAtm;
import com.github.ivanig.common.messages.ResponseToAtm;
import lombok.SneakyThrows;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class AtmServiceTest {

    private static MockWebServer mws;
    private AtmService atmService;

    @BeforeAll
    static void setUp() throws IOException {
        mws = new MockWebServer();
        mws.start();
    }

    @AfterAll
    static void tearDown() throws IOException {
        mws.shutdown();
    }

    @BeforeEach
    void init() {
        atmService = new AtmService("http://localhost:" + mws.getPort());
    }

    @SneakyThrows
    @Test
    public void successGetClientInfoFromBank() {
        RequestFromAtm request = new RequestFromAtm("fn", "ln", 16L, 1111);

        mws.enqueue(new MockResponse()
                .setBody("{\"firstname\":\"fn\",\"patronymic\":\"pa\"," +
                        "\"accountsAndBalances\":{\"20\":\"0.00 EUR\"," +
                                                 "\"21\":\"0.00 USD\"," +
                                                 "\"22\":\"0.00 RUB\"}," +
                        "\"pinCodeStatus\":\"OK\"}")
                .addHeader("Content-Type", "application/json"));

        Mono<ResponseToAtm> response = atmService.getClientInfoFromBank(request);

        StepVerifier
                .create(response)
                .expectNextMatches(r ->
                        request.getFirstName().equals(r.getFirstname()))
                .verifyComplete();

        RecordedRequest recordedRequest = mws.takeRequest();

        assertEquals("POST", recordedRequest.getMethod());
        assertEquals("/clientInfo", recordedRequest.getPath());
    }

    @Test
    public void failGetClientInfoFromBank() {

    }

    @Test
    public void successAnalyzeAndConvertToResponseForClient() {

    }

    @Test
    public void failAnalyzeAndConvertToResponseForClient() {

    }
}
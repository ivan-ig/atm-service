package com.github.ivanig.atmserver.kafka;

import com.github.ivanig.atmserver.exceptions.NotFoundException;
import com.github.ivanig.atmserver.rest.dto.ResponseToClient;
import com.github.ivanig.atmserver.service.AtmService;
import com.github.ivanig.common.messages.RequestFromAtm;
import com.github.ivanig.common.messages.ResponseToAtm;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
@AllArgsConstructor
public class AtmKafkaService {

    private KafkaTemplate<String, RequestFromAtm> kafkaTemplate;
    private AtmService atmService;
    private MessageContext messageContext;

    public void sendMessage(RequestFromAtm request) {
        kafkaTemplate.send("requests", request)
                .addCallback(new ListenableFutureCallback<SendResult<String, RequestFromAtm>>() {

                    @Override
                    public void onFailure(@NonNull Throwable ex) {
                        log.debug("Unable to send [" + request + "] due to " + ex.getMessage());
                    }

                    @Override
                    public void onSuccess(SendResult<String, RequestFromAtm> result) {
                        log.debug("Sent [" + request + "] with offset=[" + result.getRecordMetadata().offset() + "]");
                    }
                });
    }

    @KafkaListener(topics = "responses", containerFactory = "responseKafkaListenerContainerFactory")
    private void responseListener(ResponseToAtm response) {
        log.debug("Received [" + response + "]");

        ResponseToClient responseToClient = atmService.analyzeAndConvertToResponseForClient(response);

        messageContext.addMessage(responseToClient);
    }

    @SneakyThrows(value = InterruptedException.class)
    public ResponseToClient awaitMessage(String id) {

        ResponseToClient response =  messageContext.getMessage(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.REQUEST_TIMEOUT, "Response [" + id + "] time expired"));

        log.debug("Successfully retrieved from queue [" + response + "]");

        return checkForNotFoundExceptions(response);
    }

    private ResponseToClient checkForNotFoundExceptions(ResponseToClient response) {
        if ("CLIENT NOT_FOUND".equals(response.getClientName())) {
            throw new NotFoundException("Unable to find client [" + response.getClientName() + "]; request id [" +
                    response.getId() + "]");
        }
        if ("CARD NOT_FOUND".equals(response.getClientName())) {
            throw new NotFoundException("Unable to find client with such card; request id [" + response.getId() + "]");
        }
        return response;
    }
}
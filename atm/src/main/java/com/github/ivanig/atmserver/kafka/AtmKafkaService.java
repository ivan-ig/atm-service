package com.github.ivanig.atmserver.kafka;

import com.github.ivanig.atmserver.rest.dto.ResponseToClient;
import com.github.ivanig.atmserver.service.AtmService;
import com.github.ivanig.common.messages.RequestFromAtm;
import com.github.ivanig.common.messages.ResponseToAtm;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.concurrent.SynchronousQueue;

@Slf4j
@Service
@AllArgsConstructor
public class AtmKafkaService {

    private KafkaTemplate<String, RequestFromAtm> kafkaTemplate;
    private AtmService atmService;
    private SynchronousQueue<ResponseToClient> synchronousQueue;

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

        try {
            synchronousQueue.put(responseToClient);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @SneakyThrows
    public ResponseToClient awaitMessage() {
        return synchronousQueue.take();
    }
}

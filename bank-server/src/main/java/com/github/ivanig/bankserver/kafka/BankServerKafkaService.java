package com.github.ivanig.bankserver.kafka;

import com.github.ivanig.bankserver.service.BankServerService;
import com.github.ivanig.common.messages.RequestFromAtm;
import com.github.ivanig.common.messages.ResponseToAtm;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Slf4j
@Service
@AllArgsConstructor
public class BankServerKafkaService {

    private KafkaTemplate<String, ResponseToAtm> kafkaTemplate;
    private BankServerService bankServerService;

    @KafkaListener(topics = "requests", containerFactory = "requestKafkaListenerContainerFactory")
    private void requestListener(RequestFromAtm request) {

        ResponseToAtm response = bankServerService.getCardAccountsInfoAndConvertToResponse(request);
        log.debug("Received [" + response + "]");
        sendMessage(response);
    }

    private void sendMessage(ResponseToAtm response) {
        kafkaTemplate.send("responses", response)
                .addCallback(new ListenableFutureCallback<SendResult<String, ResponseToAtm>>() {
                    @Override
                    public void onFailure(@NonNull Throwable ex) {
                        log.debug("Unable to send [" + response + "] due to " + ex.getMessage());
                    }

                    @Override
                    public void onSuccess(SendResult<String, ResponseToAtm> result) {
                        log.debug("Sent [" + response + "] with offset=[" + result.getRecordMetadata().offset() + "]");
                    }
                });
    }

}

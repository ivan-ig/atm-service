package com.github.ivanig.atmserver.kafka;

import com.github.ivanig.atmserver.rest.dto.ResponseToClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class MessageContext {

    @Value("${atmService.timeoutMillisecondValue}")
    private int timeout;

    private final Map<String, ArrayBlockingQueue<ResponseToClient>> context = new ConcurrentHashMap<>();

    private BlockingQueue<ResponseToClient> getQueue(String id) {
        return context.compute(id, (k, v) -> (v == null) ? new ArrayBlockingQueue<>(1) : v);
    }

    public void addMessage(ResponseToClient response) {
            boolean offerResult = getQueue(response.getId()).offer(response);
            log.debug("Queue offer result [" + offerResult + "] with [" + response + "]");
    }

    public Optional<ResponseToClient> getMessage(String id) throws InterruptedException {
        return Optional.ofNullable(getQueue(id).poll(timeout, TimeUnit.MILLISECONDS));
    }
}
package com.github.ivanig.atmserver.configurations.kafka;

import com.github.ivanig.atmserver.rest.dto.ResponseToClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.SynchronousQueue;

@Configuration
public class QueueConfiguration {

    @Bean
    public SynchronousQueue<ResponseToClient> toClientSynchronousQueueBean() {
        return new SynchronousQueue<>();
    }
}

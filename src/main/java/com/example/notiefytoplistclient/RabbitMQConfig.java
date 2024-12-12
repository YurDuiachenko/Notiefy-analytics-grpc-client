package com.example.notiefytoplistclient;

import lombok.Getter;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Getter
@Configuration
public class RabbitMQConfig {
    @Value("${rabbitmq.song-count.queue}")
    private String songCountQueueName;

    @Bean
    public Queue songCountQueue() {
        return new Queue(songCountQueueName, false);
    }
}
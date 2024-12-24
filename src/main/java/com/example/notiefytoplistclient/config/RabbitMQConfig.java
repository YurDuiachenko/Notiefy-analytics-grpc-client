package com.example.notiefytoplistclient.config;

import lombok.Getter;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Getter
@Configuration
public class RabbitMQConfig {
    @Value("${rabbitmq.song-count.queue}")
    private String songCountQueueName;
    @Value("${rabbitmq.song-analytics.queue}")
    private String songAnalyticsQueueName;
    @Value("${rabbitmq.song-analytics.exchange}")
    private String songAnalyticsExchangeName;
    @Value("${rabbitmq.song-analytics.key}")
    private String songAnalyticsKey;

    @Bean
    public Queue songCountQueue() {
        return new Queue(songCountQueueName, false);
    }

    @Bean
    public Queue songAnalyticsQueue() {
        return new Queue(songAnalyticsQueueName, false);
    }

    @Bean
    public Exchange songAnalyticsExchange() {
        return new TopicExchange(songAnalyticsExchangeName, false, false);
    }

    @Bean
    public Binding songAnalyticsBinding(Queue songAnalyticsQueue, Exchange songAnalyticsExchange) {
        return BindingBuilder.bind(songAnalyticsQueue)
                .to(songAnalyticsExchange)
                .with(songAnalyticsKey)
                .noargs();
    }
}
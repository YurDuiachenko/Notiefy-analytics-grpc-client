package com.example.notiefytoplistclient.rabbitmq;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SongAnalyticsEventSupplier {
    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.song-analytics.exchange}")
    private String songAnalyticsExchangeName;
    @Value("${rabbitmq.song-analytics.key}")
    private String songAnalyticsKey;

    /**
     * Отправить отчёт по аналитике
     *
     * @param message отчёт по аналитике
     */
    public void supply(String message) {
        System.out.println(message);
        rabbitTemplate.convertAndSend(songAnalyticsExchangeName, songAnalyticsKey, message);
    }
}
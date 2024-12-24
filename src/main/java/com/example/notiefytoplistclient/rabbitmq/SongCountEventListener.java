package com.example.notiefytoplistclient.rabbitmq;

import com.example.notiefytoplistclient.domain.SongCount;
import com.example.notiefytoplistclient.service.AnalyticsService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class SongCountEventListener {
    private static final String queueName = "songCount";

    private final ObjectMapper objectMapper;
    private final AnalyticsService analyticsService;

    @RabbitListener(queues = queueName)
    public void listen(String massage) {
        List<SongCount> songs;
        try {
            System.out.println(massage);
            songs = objectMapper.readValue(massage, new TypeReference<List<SongCount>>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        log.info("Message read from songCount : {}", songs);

        analyticsService.validateAnalytics(songs);
    }
}
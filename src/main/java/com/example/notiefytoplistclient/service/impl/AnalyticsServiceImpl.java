package com.example.notiefytoplistclient.service.impl;

import com.example.grpc.SongServiceProto;
import com.example.notiefytoplistclient.domain.SongCount;
import com.example.notiefytoplistclient.grpc.ValidationServiceClient;
import com.example.notiefytoplistclient.rabbitmq.SongAnalyticsEventSupplier;
import com.example.notiefytoplistclient.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnalyticsServiceImpl implements AnalyticsService {
    private final ValidationServiceClient validationServiceClient = new ValidationServiceClient("localhost", 8080);
    private final SongAnalyticsEventSupplier songAnalyticsEventSupplier;

    @Override
    public void validateAnalytics(List<SongCount> songCounts) {

        SongServiceProto.AnalyticsResponse songResponse = validationServiceClient.validateAnalytics(
                songCounts.stream().map(
                        songCount -> SongServiceProto.SongCount.newBuilder()
                        .setName(songCount.getName())
                        .setPlaysCount(songCount.getPlaysCount())
                        .build()
                ).toList()
        );

        System.out.println(songResponse.getMessage());
        if (songResponse.getSuccess()) {
            songAnalyticsEventSupplier.supply(songResponse.getMessage());
        }
    }
}
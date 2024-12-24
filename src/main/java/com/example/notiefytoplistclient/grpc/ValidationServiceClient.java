package com.example.notiefytoplistclient.grpc;

import com.example.grpc.SongServiceProto;
import com.example.grpc.ValidationServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ValidationServiceClient implements AutoCloseable {
    private final ManagedChannel channel;
    private final ValidationServiceGrpc.ValidationServiceBlockingStub blockingStub;

    public ValidationServiceClient(String host, int port) {
        this.channel = ManagedChannelBuilder
                .forAddress(host, port)
                .usePlaintext()
                .build();
        this.blockingStub = ValidationServiceGrpc.newBlockingStub(channel);
    }


    @Override
    public void close() throws Exception {
        try {
            channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Thread interrupted while shutting down channel");
        }
    }

    public SongServiceProto.AnalyticsResponse validateAnalytics(List<SongServiceProto.SongCount> songCount) {
        try {
            SongServiceProto.SongCountList songCountListMessage = SongServiceProto.SongCountList.newBuilder()
                    .addAllSongCounts(songCount)
                    .build();
            return blockingStub.validateAnalytics(songCountListMessage);
        } catch (StatusRuntimeException e) {
            System.err.println("Failed to create song: " + e.getStatus());

            return SongServiceProto.AnalyticsResponse.newBuilder()
                    .setSuccess(false)
                    .setMessage("Failed to validate analytics")
                    .build();
        }
    }
}

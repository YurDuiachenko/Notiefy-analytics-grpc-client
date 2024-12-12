package com.example.grpc;

import com.example.grpc.SongServiceGrpc.SongServiceBlockingStub;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

import java.util.concurrent.TimeUnit;

public class SongServiceClient implements AutoCloseable {
    private final ManagedChannel channel;
    private final SongServiceBlockingStub blockingStub;

    public SongServiceClient(String host, int port) {
        this.channel = ManagedChannelBuilder
                .forAddress(host, port)
                .usePlaintext()  // Отключение SSL
                .build();
        this.blockingStub = SongServiceGrpc.newBlockingStub(channel);
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



    // Демонстрация всех CRUD операций
    public void demonstrateAllOperations() {
        try {
            System.out.println("=== Demonstrating SongService Operations ===");

            // 1. Создание новой песни
            SongServiceProto.SongCount newSong = SongServiceProto.SongCount.newBuilder()
                    .setName("Song1")
                    .setPlaysCount(100)
                    .build();
            try {
                blockingStub.createSong(newSong);
                System.out.println("Created new song: " + newSong.getName());
            } catch (StatusRuntimeException e) {
                System.err.println("Failed to create song: " + e.getStatus());
            }

            // 2. Получение всех песен
            System.out.println("\n2. Retrieving song:");
            try {
                SongServiceProto.GetSongRequest request = SongServiceProto.GetSongRequest.newBuilder().setName("Song1").build();
                SongServiceProto.SongCount song = blockingStub.getSong(request);
                System.out.println("Found song: " + song.getName() + " with plays count: " + song.getPlaysCount());
            } catch (StatusRuntimeException e) {
                System.err.println("Failed to get song: " + e.getStatus());
            }

            // 3. Обновление песни
            System.out.println("\n3. Updating song:");
            SongServiceProto.SongCount updatedSong = SongServiceProto.SongCount.newBuilder(newSong)
                    .setPlaysCount(150)  // Обновление счетчика воспроизведений
                    .build();
            try {
                blockingStub.updateSong(updatedSong);
                System.out.println("Updated song plays count to: " + updatedSong.getPlaysCount());
            } catch (StatusRuntimeException e) {
                System.err.println("Failed to update song: " + e.getStatus());
            }

            // 4. Удаление песни
            System.out.println("\n4. Deleting song:");
            try {
                SongServiceProto.GetSongRequest deleteRequest = SongServiceProto.GetSongRequest.newBuilder().setName("Song1").build();
                blockingStub.deleteSong(deleteRequest);
                System.out.println("Deleted song: " + updatedSong.getName());
            } catch (StatusRuntimeException e) {
                System.err.println("Failed to delete song: " + e.getStatus());
            }

            // Небольшая пауза перед завершением
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Client interrupted: " + e.getMessage());
        }
    }
}

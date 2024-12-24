package com.example.notiefytoplistclient.service;

import com.example.notiefytoplistclient.domain.SongCount;

import java.util.List;

public interface AnalyticsService {
    void validateAnalytics(List<SongCount> songCounts);
}
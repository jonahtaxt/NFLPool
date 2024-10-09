package com.effisoft.nflpool.interfaces;

import com.effisoft.nflpool.model.GameScore;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface NFLCrawler {
    CompletableFuture<List<GameScore>> asyncGetWeekScore(int year, int week);
}

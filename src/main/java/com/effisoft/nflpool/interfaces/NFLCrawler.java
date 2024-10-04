package com.effisoft.nflpool.interfaces;

import com.effisoft.nflpool.model.GameScore;
import org.springframework.scheduling.annotation.Async;

import java.util.List;
import java.util.concurrent.Future;

public interface NFLCrawler {
    Future<List<GameScore>> asyncGetWeekScore(int year, int week);
}

package com.effisoft.nflpool.interfaces;

import com.effisoft.nflpool.model.WeekResults;

import java.util.concurrent.CompletableFuture;

public interface PoolService {
    CompletableFuture<WeekResults> asyncGetWeekResults(int year, int week);
}

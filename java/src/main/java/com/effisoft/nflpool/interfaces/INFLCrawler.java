package com.effisoft.nflpool.interfaces;

import com.effisoft.nflpool.model.GameScore;

import java.util.List;

public interface INFLCrawler {
    public List<GameScore> getWeekScore(int year, int week);
}

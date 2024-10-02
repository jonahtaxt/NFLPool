package com.effisoft.nflpool.controllers;

import com.effisoft.nflpool.interfaces.NFLCrawler;
import com.effisoft.nflpool.model.GameScore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ScoreController {

    private final NFLCrawler nflCrawler;

    public ScoreController(NFLCrawler nflCrawler) {
        this.nflCrawler = nflCrawler;
    }

    @GetMapping("/scores/{year}/{week}")
    String GetWeekScores(@PathVariable int year, @PathVariable int week) {
        List<GameScore> test = this.nflCrawler.getWeekScore(year, week);
        return "Not so hard";
    }
}

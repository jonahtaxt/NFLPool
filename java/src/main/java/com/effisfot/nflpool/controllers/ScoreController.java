package com.effisfot.nflpool.controllers;

import com.effisfot.nflpool.interfaces.INFLCrawler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ScoreController {

    private final INFLCrawler nflCrawler;

    public ScoreController(INFLCrawler nflCrawler) {
        this.nflCrawler = nflCrawler;
    }

    @GetMapping("/scores/{year}/{week}")
    String GetWeekScores(@PathVariable int year, @PathVariable int week) {
        return this.nflCrawler.getWeekScore(year,week);
    }
}

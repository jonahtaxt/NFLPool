package com.effisoft.nflpool.controllers;

import com.effisoft.nflpool.interfaces.FileReader;
import com.effisoft.nflpool.interfaces.NFLCrawler;
import com.effisoft.nflpool.model.GameScore;
import com.effisoft.nflpool.model.Participant;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ScoreController {

    private final NFLCrawler nflCrawler;
    private final FileReader excelReader;

    public ScoreController(NFLCrawler nflCrawler,
                           FileReader excelReader) {
        this.nflCrawler = nflCrawler;
        this.excelReader = excelReader;
    }

    @GetMapping("/scores/{year}/{week}")
    String GetWeekScores(@PathVariable int year, @PathVariable int week) {
        List<Participant> participants = excelReader.readParticipants(year, week);
        //List<GameScore> test = this.nflCrawler.getWeekScore(year, week);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(participants);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}

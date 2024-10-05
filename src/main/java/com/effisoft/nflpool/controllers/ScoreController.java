package com.effisoft.nflpool.controllers;

import com.effisoft.nflpool.interfaces.DatabaseAccess;
import com.effisoft.nflpool.interfaces.FileReader;
import com.effisoft.nflpool.interfaces.NFLCrawler;
import com.effisoft.nflpool.model.GameScore;
import com.effisoft.nflpool.model.Participant;
import com.effisoft.nflpool.model.ParticipantWeekData;
import com.effisoft.nflpool.model.data.DatabaseDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@RestController
public class ScoreController {

    private final NFLCrawler nflCrawler;
    private final FileReader excelReader;
    private final DatabaseAccess databaseAccess;

    public ScoreController(NFLCrawler nflCrawler,
                           FileReader excelReader,
                           DatabaseAccess databaseAccess) {
        this.nflCrawler = nflCrawler;
        this.excelReader = excelReader;
        this.databaseAccess = databaseAccess;
    }

    @GetMapping("/scores/{year}/{week}")
    String GetWeekScores(@PathVariable int year, @PathVariable int week) throws ExecutionException, InterruptedException, JsonProcessingException {
        CompletableFuture<ParticipantWeekData> futureParticipantWeekData = this.databaseAccess.GetById(ParticipantWeekData.class,"ParticipantWeekData", "2024.4");
        CompletableFuture<List<GameScore>> completableFutureGameScore = this.nflCrawler.asyncGetWeekScore(year, week);
        CompletableFuture<Void> combinedFuture = CompletableFuture.allOf(futureParticipantWeekData, completableFutureGameScore);
        combinedFuture.get();
        ParticipantWeekData participants = futureParticipantWeekData.get();
        List<GameScore> weekGameScores = completableFutureGameScore.get();
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(participants);
    }

    @PostMapping("/scores/")
    void PostWeekParticipants(@RequestParam("file")MultipartFile file,
                                int year, int week) {
        try(InputStream fileStream = file.getInputStream()) {
            this.databaseAccess.Upsert(new DatabaseDTO<>(excelReader.readParticipants(fileStream), "ParticipantWeekData",
                    String.format("%s.%s", year, week)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

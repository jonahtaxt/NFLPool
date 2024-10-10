package com.effisoft.nflpool.controllers;

import com.effisoft.nflpool.configuration.CouchbaseConfiguration;
import com.effisoft.nflpool.interfaces.DatabaseAccess;
import com.effisoft.nflpool.interfaces.FileReader;
import com.effisoft.nflpool.interfaces.PoolService;
import com.effisoft.nflpool.model.WeekResults;
import com.effisoft.nflpool.model.data.DatabaseDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
public class ScoreController {

    @Autowired
    private CouchbaseConfiguration configuration;
    private final PoolService poolService;
    private final DatabaseAccess databaseAccess;
    private final FileReader excelReader;

    public ScoreController(PoolService poolService,
                           DatabaseAccess databaseAccess,
                           FileReader excelReader) {
        this.poolService = poolService;
        this.databaseAccess = databaseAccess;
        this.excelReader = excelReader;
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping("/scores/{year}/{week}")
    String GetWeekScores(@PathVariable int year, @PathVariable int week) throws ExecutionException, InterruptedException, JsonProcessingException {

        CompletableFuture<WeekResults> weekResults = this.poolService.asyncGetWeekResults(year, week);

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(weekResults.get());
    }

    @PostMapping("/scores/")
    void PostWeekParticipants(@RequestParam("file")MultipartFile file,
                                int year, int week) {
        try(InputStream fileStream = file.getInputStream()) {
            this.databaseAccess.upsert(new DatabaseDTO<>(excelReader.readParticipants(fileStream), "ParticipantWeekData",
                    String.format("%s.%s", year, week)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

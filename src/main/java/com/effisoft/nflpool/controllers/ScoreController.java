package com.effisoft.nflpool.controllers;

import com.effisoft.nflpool.interfaces.DatabaseAccess;
import com.effisoft.nflpool.interfaces.FileReader;
import com.effisoft.nflpool.interfaces.NFLCrawler;
import com.effisoft.nflpool.model.data.DatabaseDTO;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

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
    String GetWeekScores(@PathVariable int year, @PathVariable int week) {
        return "testing";
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

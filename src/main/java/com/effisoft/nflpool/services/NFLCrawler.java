package com.effisoft.nflpool.services;

import com.effisoft.nflpool.model.GameScore;
import com.effisoft.nflpool.model.teams.Team;
import com.effisoft.nflpool.services.exceptions.NFLCrawlerException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

@Service
public class NFLCrawler implements com.effisoft.nflpool.interfaces.NFLCrawler {

    private static final String SCORE_ROW_SELECTOR =
            "section .Scoreboard.bg-clr-white.flex.flex-auto.justify-between";

    private static final String TEAMS_SELECTOR = "div .ScoreCell__TeamName.ScoreCell__TeamName--shortDisplayName";

    private static final String SCORE_SELECTOR =
            "div .ScoreCell__Score.h4.clr-gray-01.fw-heavy.tar.ScoreCell_Score--scoreboard.pl2";

    private static final String NFL_URL = "https://www.espn.com/nfl/scoreboard/_/week/%s/year/%s/seasontype/2";

    @Override
    @Async
    public CompletableFuture<List<GameScore>> asyncGetWeekScore(int year, int week) {
        List<GameScore> weekScores = new ArrayList<>();
        CompletableFuture<List<GameScore>> completableFuture = new CompletableFuture<>();
        String nflScoreUrl = String.format(NFL_URL, week, year);
        Executors.newCachedThreadPool().submit(() -> {
            try {
                Document nflWebDoc = Jsoup.connect(nflScoreUrl).header("Cache-Control", "no-cache") // No caching
                        .header("Pragma", "no-cache")        // For HTTP/1.0
                        .header("Expires", "0")
                        .get();
                int gameScoreId = 1;
                for (Element scoreRow : nflWebDoc.select(SCORE_ROW_SELECTOR)) {
                    Team awayTeam = null;
                    Team homeTeam = null;
                    int awayScore = 0;
                    int homeScore = 0;
                    Elements teams = scoreRow.children().select(TEAMS_SELECTOR);
                    Elements scores = scoreRow.children().select(SCORE_SELECTOR);

                    if (!teams.isEmpty()) {
                        awayTeam = this.InstantiateTeam(teams.getFirst().text());
                        homeTeam = this.InstantiateTeam(teams.getLast().text());
                    }
                    if (!scores.isEmpty()) {
                        awayScore = scores.getFirst().text().isEmpty() ? 0 : Integer.parseInt(scores.getFirst().text());
                        homeScore = scores.getLast().text().isEmpty() ? 0 : Integer.parseInt(scores.getLast().text());
                    }

                    weekScores.add(new GameScore(gameScoreId, homeTeam, awayTeam, homeScore, awayScore));

                    gameScoreId += 1;
                }

            } catch (IOException | RuntimeException e) {
                completableFuture.completeExceptionally(new NFLCrawlerException("An Error occurred while retrieving NFL's weekly results", e));
                return null;
            }
            completableFuture.complete(weekScores);
            return null;
        });
        return completableFuture;
    }

    private Team InstantiateTeam(String teamName) throws RuntimeException {
        try {
            String fullClassName = "com.effisoft.nflpool.model.teams.";
            if (teamName.contains("49ers")) {
                fullClassName += "_49ers";
            } else {
                fullClassName += teamName;
            }
            return (Team) Class.forName(fullClassName).getConstructor().newInstance();
        } catch (ClassNotFoundException | NoSuchMethodException |
                 InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}

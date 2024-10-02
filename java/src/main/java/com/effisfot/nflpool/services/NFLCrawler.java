package com.effisfot.nflpool.services;

import com.effisfot.nflpool.interfaces.INFLCrawler;
import com.effisfot.nflpool.model.Team;
import com.effisfot.nflpool.model._49ers;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class NFLCrawler implements INFLCrawler {

    private static final String SCORE_ROW_SELECTOR =
            "section .Scoreboard.bg-clr-white.flex.flex-auto.justify-between";

    private static final String TEAMS_SELECTOR = "div .ScoreCell__TeamName.ScoreCell__TeamName--shortDisplayName";

    private static final String SCORE_SELECTOR =
            "div .ScoreCell__Score.h4.clr-gray-01.fw-heavy.tar.ScoreCell_Score--scoreboard.pl2";


    @Override
    public String getWeekScore(int year, int week) {
        String nflScoreUrl = String.format("https://www.espn.com/nfl/scoreboard/_/week/%s/year/%s/seasontype/2", week, year);
        try {
            Document nflWebDoc = Jsoup.connect(nflScoreUrl).get();
            nflWebDoc.select(SCORE_ROW_SELECTOR).forEach(scoreRow -> {
                Elements teams = scoreRow.children().select(TEAMS_SELECTOR);
                Elements scores = scoreRow.children().select(SCORE_SELECTOR);

                if(!teams.isEmpty()) {
                    Team awayTeam = this.InstantiateTeam(teams.getFirst().text());
                    Team homeTeam = this.InstantiateTeam(teams.getLast().text());
                }
                
            });

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "it is working";
    }

    private Team InstantiateTeam(String teamName)  {
        try {
            String fullClassName = "com.effisfot.nflpool.model.";
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

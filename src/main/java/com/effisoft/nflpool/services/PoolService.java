package com.effisoft.nflpool.services;

import com.effisoft.nflpool.interfaces.DatabaseAccess;
import com.effisoft.nflpool.interfaces.NFLCrawler;
import com.effisoft.nflpool.model.GameScore;
import com.effisoft.nflpool.model.Participant;
import com.effisoft.nflpool.model.ParticipantWeekData;
import com.effisoft.nflpool.model.WeekResults;
import com.effisoft.nflpool.model.data.DatabaseDTO;
import com.effisoft.nflpool.services.exceptions.PoolServiceException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

@Service
public class PoolService implements com.effisoft.nflpool.interfaces.PoolService {

    private final NFLCrawler nflCrawler;;
    private final DatabaseAccess databaseAccess;

    public PoolService(NFLCrawler nflCrawler,
                       DatabaseAccess databaseAccess) {
        this.nflCrawler = nflCrawler;
        this.databaseAccess = databaseAccess;
    }

    @Override
    public CompletableFuture<WeekResults> asyncGetWeekResults(int year, int week) {
        CompletableFuture<WeekResults> result = new CompletableFuture<>();

        Executors.newCachedThreadPool().submit(() -> {
            CompletableFuture<ParticipantWeekData> futureParticipantWeekData = this.databaseAccess.asyncGetById(ParticipantWeekData.class,"ParticipantWeekData",
                    String.format("%s.%s", year, week));
            CompletableFuture<List<GameScore>> completableFutureGameScore = this.nflCrawler.asyncGetWeekScore(year, week);
            CompletableFuture<Void> combinedFuture = CompletableFuture.allOf(futureParticipantWeekData, completableFutureGameScore);
            try {
                combinedFuture.get();
                ParticipantWeekData participants = futureParticipantWeekData.get();
                List<GameScore> weekGameScores = completableFutureGameScore.get();

                WeekResults weekResults = new WeekResults(weekGameScores,
                        participants != null ? participants.getParticipants() : null,
                        null);

                if(participants != null) {
                    List<String> mondayNightTeams = participants.getMondayNightTeams();

                    CalculateTotalPoints(weekResults);

                    if (mondayNightTeams != null) {
                        GameScore mondayNightGame = CalculateMondayNightPoints(weekResults, mondayNightTeams);
                        OrderParticipantsByWinner(weekResults);
                        if(mondayNightGame != null) {
                            if (mondayNightGame.getAwayScore() > 0 || mondayNightGame.getHomeScore() > 0) {
                                weekResults.setParticipantWinnerId(weekResults.getParticipants().getFirst().getId());
                            }
                        }
                    }

                    this.databaseAccess.upsert(new DatabaseDTO<>(new ParticipantWeekData(weekResults.getParticipants(), mondayNightTeams), "ParticipantWeekData",
                            String.format("%s.%s", year, week)));
                }

                result.complete(weekResults);
                return null;
            } catch (InterruptedException | ExecutionException e) {
                result.completeExceptionally(new PoolServiceException("An error occurred during retrieval of Week Results", e));
                return null;
            }
        });
        return result;
    }

    private static void CalculateTotalPoints(WeekResults weekResults) {
        if (weekResults.getParticipants() == null) return;

        weekResults.getParticipants().forEach(participant -> { participant.setTotalPoints(0); });

        weekResults.getGameScores().stream()
                .map(GameScore::getWinningTeam)
                .filter(Objects::nonNull)
                .forEach(winningTeam -> {
                    weekResults.getParticipants().forEach(participant -> {
                        if(participant.getBets().stream()
                                .anyMatch(bet -> Objects.equals(bet, winningTeam.toUpperCase()))) {
                            participant.addTotalPoints();
                        }
                    });
                });
    }

    private static GameScore CalculateMondayNightPoints(WeekResults weekResults,
                                                        List<String> mondayNightTeams) {
        if (weekResults.getParticipants() == null) return null;

        GameScore mondayNightGame = weekResults.getGameScores().stream()
                .filter(gameScore -> Objects.equals(gameScore.getHomeTeam().getName().toUpperCase(), mondayNightTeams.getLast()) &&
                        Objects.equals(gameScore.getAwayTeam().getName().toUpperCase(), mondayNightTeams.getFirst()))
                .findFirst()
                .orElseGet(() -> null);

        if(mondayNightGame == null) return null;

        if(mondayNightGame.getAwayScore() <= 0 && mondayNightGame.getHomeScore() <= 0) return mondayNightGame;

        int mondayNightScoreDifference = mondayNightGame.getAwayScore() + mondayNightGame.getHomeScore();

        weekResults.getParticipants().forEach(participant -> {
            int pointDifferce = mondayNightScoreDifference - participant.getMondayNightPoints();
            if(pointDifferce < 0) pointDifferce *=-1;
            participant.setMondayNightPointsDifference(pointDifferce);
        });

        return mondayNightGame;
    }

    private static void OrderParticipantsByWinner(WeekResults weekResults) {
        if (weekResults.getParticipants() == null) return;

        List<String> orderedBets = new ArrayList<>();
        weekResults.getParticipants().forEach(participant -> {

            weekResults.getGameScores().forEach(gameScore -> {
                if(participant.getBets().stream()
                        .anyMatch(bet -> gameScore.getAwayTeam().getName().toUpperCase().equals(bet))) {
                    if (gameScore.getAwayTeam() != null) {
                        orderedBets.add(gameScore.getAwayTeam().getName().toUpperCase());
                    }
                } else if (participant.getBets().stream()
                        .anyMatch(bet -> gameScore.getHomeTeam().getName().toUpperCase().equals(bet))) {
                    if (gameScore.getHomeTeam() != null) {
                        orderedBets.add(gameScore.getHomeTeam().getName().toUpperCase());
                    }
                } else {
                    orderedBets.add("");
                }
            });
            participant.setOrderedBets(orderedBets);
            orderedBets.clear();
        });

        weekResults.setParticipants(weekResults.getParticipants().stream()
                .sorted(Comparator.comparingInt(Participant::getTotalPoints).reversed()
                        .thenComparingInt(Participant::getMondayNightPointsDifference)
                        .thenComparingInt(Participant::getId))
                .toList());
    }
}

package com.effisoft.nflpool.services;

import com.effisoft.nflpool.interfaces.DatabaseAccess;
import com.effisoft.nflpool.interfaces.NFLCrawler;
import com.effisoft.nflpool.model.GameScore;
import com.effisoft.nflpool.model.ParticipantWeekData;
import com.effisoft.nflpool.model.WeekResults;
import com.effisoft.nflpool.services.exceptions.PoolServiceException;
import org.springframework.stereotype.Service;

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
                CalculateTotalPoints(weekResults);

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
                .filter(winningTeam -> winningTeam != null)
                .forEach(winningTeam -> {
                    weekResults.getParticipants().forEach(participant -> {
                        if(participant.getBets().stream()
                                .anyMatch(bet -> Objects.equals(bet, winningTeam.toUpperCase()))) {
                            participant.addTotalPoints();
                        }
                    });
                });
    }
}

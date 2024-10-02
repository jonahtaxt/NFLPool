package com.effisoft.nflpool.model;

public class GameScore {

    private final int gameScoreId;
    private final Team homeTeam;
    private final Team awayTeam;
    private final int homeScore;
    private final int awayScore;

    public GameScore(int gameScoreId, Team homeTeam, Team awayTeam,
                     int homeScore, int awayScore) {
        this.gameScoreId = gameScoreId;
        this.homeTeam = homeTeam;
        this.homeScore = homeScore;
        this.awayTeam = awayTeam;
        this.awayScore = awayScore;
    }

    public int getGameScoreId() {
        return gameScoreId;
    }

    public Team getHomeTeam() {
        return homeTeam;
    }

    public Team getAwayTeam() {
        return awayTeam;
    }

    public int getHomeScore() {
        return homeScore;
    }

    public int getAwayScore() {
        return awayScore;
    }

    public String getWinningTeam() {
        if(this.awayScore > this.homeScore) {
            return awayTeam.getPoolName();
        }
        if(this.awayScore < this.homeScore) {
            return homeTeam.getPoolName();
        }
        return null;
    }
}

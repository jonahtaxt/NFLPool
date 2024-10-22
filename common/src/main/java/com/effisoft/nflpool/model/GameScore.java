package com.effisoft.nflpool.model;

import com.effisoft.nflpool.model.teams.Team;

public record GameScore(int gameScoreId, Team homeTeam, Team awayTeam, int homeScore, int awayScore) {

    public String getWinningTeam() {
        if (this.awayScore > this.homeScore) {
            return awayTeam.getName();
        }
        if (this.awayScore < this.homeScore) {
            return homeTeam.getName();
        }
        return null;
    }
}

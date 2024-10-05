package com.effisoft.nflpool.model;

import java.util.List;

public class WeekResults {
    public List<GameScore> getGameScores() {
        return gameScores;
    }

    public List<Participant> getParticipants() {
        return participants;
    }

    public Integer getParticipantWinnerId() {
        return participantWinnerId;
    }

    public void setParticipants(List<Participant> participants) {
        this.participants = participants;
    }

    public void setParticipantWinnerId(Integer participantWinnerId) {
        this.participantWinnerId = participantWinnerId;
    }

    private final List<GameScore> gameScores;
    private List<Participant> participants;
    private Integer participantWinnerId;

    public WeekResults(List<GameScore> gameScores,
                       List<Participant>participants,
                       Integer participantWinnerId)
    {
        this.gameScores = gameScores;
        this.participants = participants;
        this.participantWinnerId = null;
    }
}

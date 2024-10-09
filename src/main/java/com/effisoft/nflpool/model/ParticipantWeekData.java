package com.effisoft.nflpool.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ParticipantWeekData {

    public List<Participant> getParticipants() {
        return participants;
    }

    public List<String> getMondayNightTeams() {
        return mondayNightTeams;
    }

    private List<Participant> participants;
    private List<String> mondayNightTeams;

    public ParticipantWeekData() {}

    @JsonCreator
    public ParticipantWeekData(@JsonProperty("participants") List<Participant> participants,
                               @JsonProperty("mondayNightTeams") List<String> mondayNightTeams) {
        this.participants = participants;
        this.mondayNightTeams = mondayNightTeams;
    }
}

package com.effisoft.nflpool.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Participant {

    private final int id;
    private final String name;
    private final int mondayNightPoints;
    private int mondayNightPointsDifference;
    private final List<String> bets;
    private int totalPoints;

    @JsonCreator
    public Participant(@JsonProperty("id") int id,
                       @JsonProperty("name") String name,
                       @JsonProperty("mondayNightPoints") int mondayNightPoints,
                       @JsonProperty("bets") List<String> bets) {
        this.id = id;
        this.name = name;
        this.bets = bets;
        this.mondayNightPoints = mondayNightPoints;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getMondayNightPoints() {
        return mondayNightPoints;
    }

    public int getMondayNightPointsDifference() {
        return mondayNightPointsDifference;
    }

    public void setMondayNightPointsDifference(int mondayNightPointsDifference) {
        this.mondayNightPointsDifference = mondayNightPointsDifference;
    }

    public List<String> getBets() {
        return bets;
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(int totalPoints) {
        this.totalPoints = totalPoints;
    }

    public void addTotalPoints() {
        this.totalPoints ++;
    }

    public void setOrderedBets(List<String> orderedBets) {
        this.bets.clear();
        this.bets.addAll(orderedBets);
    }
}

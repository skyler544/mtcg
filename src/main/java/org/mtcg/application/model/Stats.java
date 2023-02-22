package org.mtcg.application.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Stats {
    @JsonProperty("Name")
    private String name;

    @JsonProperty("ELO")
    private double elo;

    @JsonProperty("Wins")
    private double wins;

    @JsonProperty("Losses")
    private double losses;

    public Stats() {}

    public Stats(String name, double elo, double wins, double losses) {
        this.name = name;
        this.elo = elo;
        this.wins = wins;
        this.losses = losses;
    }
}

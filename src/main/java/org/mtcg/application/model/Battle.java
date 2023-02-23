package org.mtcg.application.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Battle {
    @JsonProperty("BattleId")
    private int battleId;

    @JsonProperty("PlayerOne")
    private String playerOne;
    @JsonProperty("PlayerTwo")
    private String playerTwo;

	@JsonProperty("Result")
    private String result;

    @JsonProperty("Rounds")
    private List<BattleRound> rounds;

    public Battle() {}

    public Battle(int battleId, String playerOne, String playerTwo, String result) {
		this.battleId = battleId;
		this.playerOne = playerOne;
		this.playerTwo = playerTwo;
		this.result = result;
    }

	public String getPlayerOne() {
		return playerOne;
	}

	public void setPlayerOne(String playerOne) {
		this.playerOne = playerOne;
	}

	public String getPlayerTwo() {
		return playerTwo;
	}

	public void setPlayerTwo(String playerTwo) {
		this.playerTwo = playerTwo;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public List<BattleRound> getRounds() {
		return rounds;
	}

	public void setRounds(List<BattleRound> rounds) {
		this.rounds = rounds;
	}
}

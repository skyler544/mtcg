package org.mtcg.application.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Battle {
    @JsonProperty("BattleId")
    private int battleId;

    @JsonProperty("PlayerOne")
    private String playerOne;
    @JsonProperty("PlayerTwo")
    private String playerTwo;

    @JsonProperty("RoundsId")
    private int roundsId;

	@JsonProperty("Result")
    private String result;

    public Battle() {}

    public Battle(int battleId, String playerOne, String playerTwo, int roundsId, String result) {
		this.battleId = battleId;
		this.playerOne = playerOne;
		this.playerTwo = playerTwo;
		this.roundsId = roundsId;
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

    public int getRoundsId() {
		return roundsId;
	}

	public void setRoundsId(int roundsId) {
		this.roundsId = roundsId;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
}

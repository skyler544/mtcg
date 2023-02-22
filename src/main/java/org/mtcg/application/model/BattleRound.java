package org.mtcg.application.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BattleRound {
    @JsonProperty("RoundId")
    private int roundId;
    @JsonProperty("BattleId")
    private int battleId;

    @JsonProperty("PlayerOneCardId")
    private String playerOneCardId;
    @JsonProperty("PlayerTwoCardId")
    private String playerTwoCardId;

    @JsonProperty("Result")
    String result;

    public BattleRound() {}

    public BattleRound(int roundId, int battleId, String playerOneCardId, String playerTwoCardId, String result) {
		this.roundId = roundId;
		this.battleId = battleId;
		this.playerOneCardId = playerOneCardId;
		this.playerTwoCardId = playerTwoCardId;
		this.result = result;
    }

	public int getRoundId() {
		return roundId;
	}

	public void setRoundId(int roundId) {
		this.roundId = roundId;
	}

	public int getBattleId() {
		return battleId;
	}

	public void setBattleId(int battleId) {
		this.battleId = battleId;
	}

	public String getPlayerOneCardId() {
		return playerOneCardId;
	}

	public void setPlayerOneCardId(String playerOneCardId) {
		this.playerOneCardId = playerOneCardId;
	}

	public String getPlayerTwoCardId() {
		return playerTwoCardId;
	}

	public void setPlayerTwoCardId(String playerTwoCardId) {
		this.playerTwoCardId = playerTwoCardId;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
}

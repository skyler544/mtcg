package org.mtcg.application.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BattleRound {
    @JsonProperty("RoundId")
    private int roundId;
    @JsonProperty("BattleId")
    private int battleId;

    @JsonProperty("PlayerOneCardInfo")
    private String playerOneCardInfo;
    @JsonProperty("PlayerTwoCardInfo")
    private String playerTwoCardInfo;

    @JsonProperty("Result")
    String result;

    public BattleRound() {}

    public BattleRound(int battleId, String playerOneCardInfo, String playerTwoCardInfo, String result) {
		this.battleId = battleId;
		this.playerOneCardInfo = playerOneCardInfo;
		this.playerTwoCardInfo = playerTwoCardInfo;
		this.result = result;
    }

    public BattleRound(int roundId, int battleId, String playerOneCardInfo, String playerTwoCardInfo, String result) {
		this.roundId = roundId;
		this.battleId = battleId;
		this.playerOneCardInfo = playerOneCardInfo;
		this.playerTwoCardInfo = playerTwoCardInfo;
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

	public String getPlayerOneCardInfo() {
		return playerOneCardInfo;
	}

	public void setPlayerOneCardInfo(String playerOneCardInfo) {
		this.playerOneCardInfo = playerOneCardInfo;
	}

	public String getPlayerTwoCardInfo() {
		return playerTwoCardInfo;
	}

	public void setPlayerTwoCardInfo(String playerTwoCardInfo) {
		this.playerTwoCardInfo = playerTwoCardInfo;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
}

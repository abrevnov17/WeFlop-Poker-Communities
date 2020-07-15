package com.weflop.GameService.Networking;

import java.util.List;

import com.weflop.GameService.Database.DomainObjects.CardPOJO;
import com.weflop.GameService.Database.DomainObjects.PlayerPOJO;

/**
 * CRUD class containing properties that are propogated to individual players
 * inside of game packets.
 * 
 * @author abrevnov
 *
 */
public class GameStatePOJO {
	private String gameId;

	private List<CardPOJO> centerCards;
	private float pot;

	private List<LimitedPlayerPOJO> otherPlayers;
	private PlayerPOJO player;

	private String idOfTurn;

	private int epoch;

	public GameStatePOJO(String gameId, List<CardPOJO> centerCards, float pot, List<LimitedPlayerPOJO> otherPlayers,
			PlayerPOJO player, String idOfTurn, int epoch) {
		this.gameId = gameId;
		this.centerCards = centerCards;
		this.pot = pot;
		this.otherPlayers = otherPlayers;
		this.player = player;
		this.idOfTurn = idOfTurn;
		this.epoch = epoch;
	}

	/* Getters and Setters */
	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	public List<CardPOJO> getCenterCards() {
		return centerCards;
	}

	public void setCenterCards(List<CardPOJO> centerCards) {
		this.centerCards = centerCards;
	}

	public float getPot() {
		return pot;
	}

	public void setPot(float pot) {
		this.pot = pot;
	}

	public List<LimitedPlayerPOJO> getOtherPlayers() {
		return otherPlayers;
	}

	public void setOtherPlayers(List<LimitedPlayerPOJO> otherPlayers) {
		this.otherPlayers = otherPlayers;
	}

	public PlayerPOJO getPlayer() {
		return player;
	}

	public void setPlayer(PlayerPOJO player) {
		this.player = player;
	}

	public String getIdOfTurn() {
		return idOfTurn;
	}

	public void setIdOfTurn(String idOfTurn) {
		this.idOfTurn = idOfTurn;
	}

	public int getEpoch() {
		return epoch;
	}

	public void setEpoch(int epoch) {
		this.epoch = epoch;
	}
}

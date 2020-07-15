package com.weflop.GameService.Database.DomainObjects;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * CRUD object containing game information as stored on database.
 * 
 * @author abrevnov
 *
 */
@Document
public class GameDocument {

	// fixed attributes

	@Id
	private String id;

	private int type;

	private long startTime;

	private float smallBlind;
	private float bigBlind;

	// dynamic attributes

	private List<CardPOJO> centerCards;

	private float pot;

	private int dealerIndex;

	private List<PlayerPOJO> players;

	private List<SpectatorPOJO> spectators;

	private HistoryPOJO history;

	// Constructors:

	public GameDocument(String id, int type, long startTime, float smallBlind, float bigBlind,
			List<CardPOJO> centerCards, float pot, int dealerIndex, List<PlayerPOJO> players,
			List<SpectatorPOJO> spectators, HistoryPOJO history) {
		super();
		this.id = id;
		this.type = type;
		this.startTime = startTime;
		this.smallBlind = smallBlind;
		this.bigBlind = bigBlind;
		this.centerCards = centerCards;
		this.pot = pot;
		this.dealerIndex = dealerIndex;
		this.players = players;
		this.spectators = spectators;
		this.history = history;
	}

	// getters and setters

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public float getSmallBlind() {
		return smallBlind;
	}

	public void setSmallBlind(float smallBlind) {
		this.smallBlind = smallBlind;
	}

	public float getBigBlind() {
		return bigBlind;
	}

	public void setBigBlind(float bigBlind) {
		this.bigBlind = bigBlind;
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

	public int getDealerIndex() {
		return dealerIndex;
	}

	public void setDealerIndex(int dealerIndex) {
		this.dealerIndex = dealerIndex;
	}

	public List<PlayerPOJO> getPlayers() {
		return players;
	}

	public void setPlayers(List<PlayerPOJO> players) {
		this.players = players;
	}

	public List<SpectatorPOJO> getSpectators() {
		return spectators;
	}

	public void setSpectators(List<SpectatorPOJO> spectators) {
		this.spectators = spectators;
	}

	public HistoryPOJO getHistory() {
		return history;
	}

	public void setHistory(HistoryPOJO history) {
		this.history = history;
	}
}

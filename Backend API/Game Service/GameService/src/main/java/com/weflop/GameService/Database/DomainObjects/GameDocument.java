package com.weflop.GameService.Database.DomainObjects;

import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.weflop.Game.GameCustomMetadata;

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

	// -1 if game has not started yet
	private long startTime;

	// dynamic attributes

	private List<CardPOJO> centerCards;

	private float pot;

	private int dealerIndex;

	private List<PlayerPOJO> players;

	private List<SpectatorPOJO> spectators;

	private HistoryPOJO history;
	
	private Map<String, Float> ledger;
	
	private GameCustomMetadata metadata;
	
	private boolean active;

	// Constructors:

	public GameDocument(String id, int type, long startTime,
			List<CardPOJO> centerCards, float pot, int dealerIndex, List<PlayerPOJO> players,
			List<SpectatorPOJO> spectators, HistoryPOJO history, Map<String, Float> ledger, GameCustomMetadata metadata, boolean active) {
		super();
		this.id = id;
		this.type = type;
		this.startTime = startTime;
		this.centerCards = centerCards;
		this.pot = pot;
		this.dealerIndex = dealerIndex;
		this.players = players;
		this.spectators = spectators;
		this.history = history;
		this.ledger = ledger;
		this.metadata = metadata;
		this.active = active;
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

	public Map<String, Float> getLedger() {
		return ledger;
	}

	public void setLedger(Map<String, Float> ledger) {
		this.ledger = ledger;
	}

	public GameCustomMetadata getMetadata() {
		return metadata;
	}

	public void setMetadata(GameCustomMetadata metadata) {
		this.metadata = metadata;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
}

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

	private String type;

	// -1 if game has not started yet
	private long startTime;

	// dynamic attributes

	private List<CardPOJO> centerCards;

	private float pot;

	private GroupPOJO group;
	
	private TurnPOJO turn;

	private HistoryPOJO history;
	
	private Map<String, Float> ledger;
	
	private GameCustomMetadata metadata;
	
	private boolean active;
	
	private int round;
	
	private int epoch;

	// Constructors:

	public GameDocument(String id, String type, long startTime,
			List<CardPOJO> centerCards, float pot, GroupPOJO group, TurnPOJO turn, HistoryPOJO history, 
			Map<String, Float> ledger, GameCustomMetadata metadata, boolean active, int round, int epoch) {
		super();
		this.id = id;
		this.type = type;
		this.startTime = startTime;
		this.centerCards = centerCards;
		this.pot = pot;
		this.group = group;
		this.turn = turn;
		this.history = history;
		this.ledger = ledger;
		this.metadata = metadata;
		this.active = active;
		this.round = round;
		this.epoch = epoch;
	}

	// getters and setters

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
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

	public GroupPOJO getGroup() {
		return group;
	}

	public void setGroup(GroupPOJO group) {
		this.group = group;
	}

	public TurnPOJO getTurn() {
		return turn;
	}

	public void setTurn(TurnPOJO turn) {
		this.turn = turn;
	}

	public int getRound() {
		return round;
	}

	public void setRound(int round) {
		this.round = round;
	}

	public int getEpoch() {
		return epoch;
	}

	public void setEpoch(int epoch) {
		this.epoch = epoch;
	}
}

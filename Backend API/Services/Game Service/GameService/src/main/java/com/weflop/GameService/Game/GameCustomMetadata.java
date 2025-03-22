package com.weflop.GameService.Game;

import org.springframework.data.annotation.PersistenceConstructor;

/**
 * Defines class that acts as a wrapper for information that users can manually
 * define when creating a game. For instance, names of tables, game owners,
 * admins, etc... should all be stored in this class.
 * 
 * @author abrevnov
 *
 */
public class GameCustomMetadata {
	private String name;
	private String description;
	private float smallBlind;
	private float bigBlind;
	private long turnDuration; // seconds
	private int tableSize;
	private String createdBy;
	private GameType type;
	private float minBuyIn;
	private float maxBuyIn;

	/**
	 * Constructor used for creating quick games.
	 * 
	 * @param smallBlind
	 * @param createdBy
	 * @param type
	 */
	public GameCustomMetadata(String name, float smallBlind, int minBuyInBB, int maxBuyInBB, String createdBy, int timeBank) {
		this.name = name;
		this.description = null;
		this.smallBlind = smallBlind;
		this.bigBlind = smallBlind * 2.00f;
		this.minBuyIn = minBuyInBB * this.bigBlind;
		this.maxBuyIn = maxBuyInBB * this.bigBlind;
		this.turnDuration = timeBank;
		this.tableSize = 9;
		this.createdBy = createdBy;
		this.type = GameType.STANDARD_REPRESENTATION;
	}

	@PersistenceConstructor
	public GameCustomMetadata(String name, String description, float smallBlind, float bigBlind, long turnDuration,
			int tableSize, String createdBy, GameType type, float minBuyIn, float maxBuyIn) {
		this.name = name;
		this.description = description;
		this.smallBlind = smallBlind;
		this.bigBlind = bigBlind;
		this.turnDuration = turnDuration;
		this.tableSize = tableSize;
		this.createdBy = createdBy;
		this.type = type;
		this.minBuyIn = minBuyIn;
		this.maxBuyIn = maxBuyIn;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public long getTurnDuration() {
		return turnDuration;
	}

	public void setTurnDuration(long turnDuration) {
		this.turnDuration = turnDuration;
	}

	public int getTableSize() {
		return tableSize;
	}

	public void setTableSize(int tableSize) {
		this.tableSize = tableSize;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public GameType getType() {
		return type;
	}

	public void setType(GameType type) {
		this.type = type;
	}

	public float getMinBuyIn() {
		return minBuyIn;
	}

	public void setMinBuyIn(float minBuyIn) {
		this.minBuyIn = minBuyIn;
	}

	public float getMaxBuyIn() {
		return maxBuyIn;
	}

	public void setMaxBuyIn(float maxBuyIn) {
		this.maxBuyIn = maxBuyIn;
	}

}

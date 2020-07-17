package com.weflop.Game;

import java.time.Duration;

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
	private Duration turnDuration;
	private int tableSize;
	private String createdBy;
	private GameType type;

	/**
	 * Constructor used for createing quick games.
	 * 
	 * @param smallBlind
	 * @param createdBy
	 * @param type
	 */
	public GameCustomMetadata(float smallBlind, String createdBy) {
		this.name = null;
		this.description = null;
		this.smallBlind = smallBlind;
		this.bigBlind = smallBlind * 2;
		this.turnDuration = Duration.ofSeconds(60);
		this.tableSize = 9;
		this.createdBy = createdBy;
		this.type = GameType.STANDARD_REPRESENTATION;
	}

	public GameCustomMetadata(String name, String description, float smallBlind, float bigBlind, Duration turnDuration,
			int tableSize, String createdBy, GameType type) {
		this.name = name;
		this.description = description;
		this.smallBlind = smallBlind;
		this.bigBlind = bigBlind;
		this.turnDuration = turnDuration;
		this.tableSize = tableSize;
		this.createdBy = createdBy;
		this.type = type;
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

	public Duration getTurnDuration() {
		return turnDuration;
	}

	public void setTurnDuration(Duration turnDuration) {
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

}

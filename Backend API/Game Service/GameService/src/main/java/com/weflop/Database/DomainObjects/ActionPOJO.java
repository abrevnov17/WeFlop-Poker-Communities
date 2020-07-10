package com.weflop.Database.DomainObjects;

import java.util.List;

/**
 * CRUD object wrapping information about user actions
 * 
 * @author abrevnov
 */
public class ActionPOJO {
	private int type;
	
	private String userId;
	
	private Float value;
	private List<CardPOJO> cards;
	private List<String> playerIds;
	
	private long timestamp;	// milliseconds since start of current epoch
	
	public ActionPOJO(int type, String userId, long timestamp) {
        this.type = type;
        this.userId = userId;
        this.setTimestamp(timestamp);
    }

	public ActionPOJO(int type, String userId, long timestamp, Float value) {
		this(type, userId, timestamp);
		this.value = value;
    }
	
	public ActionPOJO(int type, String userId, long timestamp, Float value, List<CardPOJO> cards, List<String> playerIds) {
		this(type, userId, timestamp, value);
		this.setCards(cards);
    }

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public float getValue() {
		return value;
	}

	public void setValue(Float value) {
		this.value = value;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public List<CardPOJO> getCards() {
		return cards;
	}

	public void setCards(List<CardPOJO> cards) {
		this.cards = cards;
	}

	public List<String> getPlayerIds() {
		return playerIds;
	}

	public void setPlayerIds(List<String> playerIds) {
		this.playerIds = playerIds;
	}
}

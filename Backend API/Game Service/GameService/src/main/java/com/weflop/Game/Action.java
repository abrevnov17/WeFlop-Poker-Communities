package com.weflop.Game;

import java.time.Instant;

import org.springframework.web.socket.WebSocketSession;

import com.weflop.Database.DomainObjects.ActionPOJO;

/**
 * Actions have an associated type and an optional parameter value
 * (as some actions such as raising have an associated float payload).
 * 
 * @author abrevnov
 *
 */
public class Action {
	// mandatory values
	private ActionType type;
	
	// optional values
	private String playerId; // user associated with action
	private WebSocketSession session; // session associated with action
	private Float value; // some actions have associated payload values
	
	// automatically set values
	private Instant timestamp;
	
	public Action(ActionType type, String playerId) {
		this.setType(type);
		this.setPlayerId(playerId);
		this.setTimestamp(Instant.now());
	}
	
	public Action(ActionType type, String playerId, float value) {
		this(type, playerId);
		this.setValue(value);
	}
	
	public Action(ActionType type, String playerId, WebSocketSession session) {
		this(type, playerId);
		this.setSession(session);
	}
	
	/**
	 * Provides POJO representation of Action
	 * (consumable by database).
	 * 
	 * @return Corresponding ActionPOJO instance
	 */
	public ActionPOJO toPojo() {
		return new ActionPOJO(type.getValue(), playerId, timestamp.toEpochMilli(), value);
	}
	
	/* Getters and Setters */

	public ActionType getType() {
		return type;
	}

	public void setType(ActionType type) {
		this.type = type;
	}

	public Float getValue() {
		return value;
	}

	public void setValue(Float value) {
		this.value = value;
	}

	public String getPlayerId() {
		return playerId;
	}

	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}

	public WebSocketSession getSession() {
		return session;
	}

	public void setSession(WebSocketSession session) {
		this.session = session;
	}

	public Instant getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Instant timestamp) {
		this.timestamp = timestamp;
	}
}

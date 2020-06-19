package com.weflop.Game;

import com.weflop.Database.DomainObjects.ActionPOJO;

/**
 * Actions have an associated type and an optional parameter value
 * (as some actions such as raising have an associated float payload).
 * 
 * @author abrevnov
 *
 */
public class Action {
	private ActionType type;
	private String playerId; // user associated with action
	private Float value; // some actions have associated payload values
	
	public Action(ActionType type, String playerId) {
		this.setType(type);
		this.setPlayerId(playerId);
	}
	
	public Action(ActionType type, String playerId, float value) {
		this(type, playerId);
		this.setValue(value);
	}
	
	/**
	 * Provides POJO representation of Action
	 * (consumable by database).
	 * 
	 * @return Corresponding ActionPOJO instance
	 */
	public ActionPOJO toPojo() {
		return new ActionPOJO(type.getValue(), playerId, value);
	}
	
	/* Getters and Setters */

	public ActionType getType() {
		return type;
	}

	public void setType(ActionType type) {
		this.type = type;
	}

	public float getValue() {
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
}

package com.weflop.Game;

/**
 * Actions have an associated type and an optional parameter value
 * (as some actions such as raising have an associated float payload).
 * 
 * @author abrevnov
 *
 */
public class Action {
	private ActionType type;
	private float value; // some actions have associated payload values
	
	public Action(ActionType type, float value) {
		this.setType(type);
		this.setValue(value);
	}
	
	public Action(ActionType type) {
		this(type, 0);
	}

	public ActionType getType() {
		return type;
	}

	public void setType(ActionType type) {
		this.type = type;
	}

	public float getValue() {
		return value;
	}

	public void setValue(float value) {
		this.value = value;
	}
}

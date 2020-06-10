package com.weflop.Game;

/**
 * Enum describing all supported actions a user can
 * make.
 * 
 * @author abrevnov
 *
 */
public enum Action {
	// Gameplay-related actions:
	FOLD(0),
	RAISE(1),
	CALL(2),
	// Logistics-related actions:
	SIT(3),
	EXIT(4),
	DISCONNECT(5);
	
	private final int value;

	Action(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
}
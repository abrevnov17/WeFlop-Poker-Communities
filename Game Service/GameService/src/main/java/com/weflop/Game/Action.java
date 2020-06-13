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
	TURN_TIMEOUT(3),
	// Logistics-related actions:
	SIT(4),
	EXIT(5),
	DISCONNECT(6);
	
	private final int value;

	Action(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
}
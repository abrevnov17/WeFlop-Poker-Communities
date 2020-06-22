package com.weflop.Game;

/**
 * Enum describing all supported types of actions a user can
 * make in the context of an existing game.
 * 
 * @author abrevnov
 *
 */
public enum ActionType {
	// Gameplay-related actions:
	FOLD(0),
	RAISE(1),
	CALL(2),
	CHECK(3),
	TURN_TIMEOUT(4),
	// Logistics-related actions:
	JOIN(5),
	SIT(6),
	STAND(7),
	DISCONNECT(8);
	
	private final int value;

	ActionType(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
	
	public static ActionType getTypeFromInt(int type) {
		   return ActionType.values()[type];
		}
}
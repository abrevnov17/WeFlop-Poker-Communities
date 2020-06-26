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
	START(0),
	FOLD(1),
	RAISE(2),
	CALL(3),
	CHECK(4),
	TURN_TIMEOUT(5),
	// Logistics-related actions:
	JOIN(6),
	SIT(7),
	STAND(8),
	DISCONNECT(9);
	
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
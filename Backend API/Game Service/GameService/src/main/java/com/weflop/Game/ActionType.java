package com.weflop.Game;

/**
 * Enum describing all supported types of actions a user can make in the context
 * of an existing game.
 * 
 * @author abrevnov
 *
 */
public enum ActionType {
	// Gameplay-related actions:
	START(0), FOLD(1), RAISE(2), CALL(3), CHECK(4), ALL_IN(5), TURN_TIMEOUT(6),
	// Logistics-related actions:
	JOIN(7), SIT(8), STAND(9), DISCONNECT(10), SIT_OUT_HAND(11), SIT_OUT_BB(12), POST_BIG_BLIND(13), TOP_OFF(14), CHANGE_SEAT(15),
	// Outgoing actions:
	PLAYER_DEAL(16), // cards dealt to individual player
	CENTER_DEAL(17), // cards dealt to center
	POT_WON(18), // some player has won a round (and the current pot)
	SMALL_BLIND(19), // player has paid small blind
	BIG_BLIND(20), // player has paid big blind
	BETTING_ROUND_OVER(21); // round of betting has concluded
	

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
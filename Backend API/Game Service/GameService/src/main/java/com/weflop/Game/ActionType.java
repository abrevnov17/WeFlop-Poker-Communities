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
	JOIN(7), SIT(8), STAND(9), DISCONNECT(10), SIT_OUT(11), POST_BIG_BLIND(12),
	// Outgoing actions:
	PLAYER_DEAL(13), // cards dealt to individual player
	CENTER_DEAL(14), // cards dealt to center
	POT_WON(15), // some player has won a round (and the current pot)
	SMALL_BLIND(16), // player has paid small blind
	BIG_BLIND(17), // player has paid big blind
	BETTING_ROUND_OVER(18); // round of betting has concluded
	

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
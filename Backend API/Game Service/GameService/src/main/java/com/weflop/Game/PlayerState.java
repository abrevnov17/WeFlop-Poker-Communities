package com.weflop.Game;

/**
 * PlayerState.java
 * 
 * Denotes possible states a player in a game can occupy.
 * 
 * @author abrevnov
 *
 */
public enum PlayerState {
	FOLDED(0), WAITING_FOR_TURN(1), WAITING_FOR_ROUND(2), CURRENT_TURN(3), ALL_IN(4), WATCHING(5);

	private final int value;

	PlayerState(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
}

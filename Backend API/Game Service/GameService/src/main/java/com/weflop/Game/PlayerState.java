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
	FOLDED(0), WAITING_FOR_TURN(1), CHECKED(2), WAITING_FOR_ROUND(3), CURRENT_TURN(4), ALL_IN(5), WATCHING(6);

	private final int value;

	PlayerState(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
	
	
}

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
	FOLDED(0), 
	WAITING_FOR_TURN(1),
	CHECKED(2), 
	WAITING_FOR_ROUND(3), // waiting for next round to join (posted big blind)
	CURRENT_TURN(4),
	ALL_IN(5), 
	WATCHING(6),
	SITTING_OUT(7), // sitting out (can sit out until next big blind comes around when they will be booted)
	WAITING_FOR_BIG_BLIND(8); // joins when they reach the next big blind (default state on sitting)

	private final int value;

	PlayerState(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
	
	
}

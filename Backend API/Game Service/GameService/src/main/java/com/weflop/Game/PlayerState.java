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
	// ACTIVE PLAYER STATES
	FOLDED(0), 
	WAITING_FOR_TURN(1),
	CHECKED(2), 
	CURRENT_TURN(3),
	ALL_IN(4), 
	
	// SPECATING STATES
	WATCHING(5),
	
	// INACTIVE PLAYER STATES
	WAITING_FOR_HAND(6), // waiting for next hand to join (posted big blind or returning after having sat out)
	SITTING_OUT_BB(7), // sitting out (until next big blind comes around, although can sit back in before that WITHOUT having to pay big blind)
	WAITING_FOR_BIG_BLIND(8), // joins when they reach the next big blind (default state on sitting)
	POSTING_BIG_BLIND(9); // user has agreed to post big blind on next hand

	private final int value;

	PlayerState(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
	
	
}

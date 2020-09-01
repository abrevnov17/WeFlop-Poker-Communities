package com.weflop.GameService.Game;

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
	FOLDED("FOLDED"), 
	WAITING_FOR_TURN("WAITING_FOR_TURN"),
	AUTO_CHECK_OR_FOLD("AUTO_CHECK_OR_FOLD"),
	AUTO_CALL("AUTO_CALL"),
	CHECKED("CHECKED"), 
	CURRENT_TURN("CURRENT_TURN"),
	ALL_IN("ALL_IN"),
	
	// SPECATING STATES
	WATCHING("WATCHING"),
	
	// INACTIVE PLAYER STATES
	WAITING_FOR_HAND("WAITING_FOR_HAND"), // waiting for next hand to join (posted big blind or returning after having sat out)
	SITTING_OUT_BB("SITTING_OUT_BB"), // sitting out (until next big blind comes around, although can sit back in before that WITHOUT having to pay big blind)
	WAITING_FOR_BIG_BLIND("WAITING_FOR_BIG_BLIND"), // joins when they reach the next big blind (default state on sitting)
	POSTING_BIG_BLIND("POSTING_BIG_BLIND"), // user has agreed to post big blind on next hand
	BUSTED("BUSTED"); // player did not have sufficient money to buy in

	private final String value;

	PlayerState(String value) {
		this.value = value;
	}

	public String toValue() {
		return value;
	}
	
	public static PlayerState fromValue(String value) {  
		if (value != null) {  
			for (PlayerState state : values()) {  
				if (state.value.equals(value)) {  
					return state;  
				}  
			}  
		}

		throw new IllegalArgumentException("Invalid player state: " + value);  
	}
}

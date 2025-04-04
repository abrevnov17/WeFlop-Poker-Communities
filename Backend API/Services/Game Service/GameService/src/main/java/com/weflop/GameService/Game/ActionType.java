package com.weflop.GameService.Game;

/**
 * Enum describing all supported types of actions a user can make in the context
 * of an existing game.
 * 
 * @author abrevnov
 *
 */
public enum ActionType {
	// Gameplay-related actions:
	START("START"), 
	FOLD("FOLD"), 
	RAISE("RAISE"), 
	CALL("CALL"), 
	CHECK("CHECK"), 
	ALL_IN("ALL_IN"), 
	TURN_TIMEOUT("TURN_TIMEOUT"),
	AUTO_CALL("AUTO_CALL"),
	AUTO_CHECK_OR_FOLD("AUTO_CHECK_OR_FOLD"),
	// Logistics-related actions:
	JOIN("JOIN"), 
	SIT("SIT"), 
	SIT_IN("SIT_IN"), 
	STAND("STAND"), 
	DISCONNECT("DISCONNECT"), 
	SIT_OUT_HAND("SIT_OUT_HAND"), 
	SIT_OUT_BB("SIT_OUT_BB"),
	CANCEL_BUY_IN("CANCEL_BUY_IN"),
	POST_BIG_BLIND("POST_BIG_BLIND"), 
	TOP_OFF("TOP_OFF"), 
	CHANGE_SEAT("CHANGE_SEAT"),
	SHOW_CARDS("SHOW_CARDS"),
	MUCK_CARDS("MUCK_CARDS"),
	BUSTED("BUSTED"),
	AUTO_MUCK("AUTO_MUCK"), // toggle auto-mucking
	// Outgoing actions:
	PLAYER_DEAL("PLAYER_DEAL"), // cards dealt to individual player
	CENTER_DEAL("CENTER_DEAL"), // cards dealt to center
	POT_WON("POT_WON"), // some player has won a round (and the current pot)
	SMALL_BLIND("SMALL_BLIND"), // player has paid small blind
	BIG_BLIND("BIG_BLIND"), // player has paid big blind
	BETTING_ROUND_OVER("BETTING_ROUND_OVER"), // round of betting has concluded
	OPTION_TO_SHOW_CARDS("OPTION_TO_SHOW_CARDS"), // present user option to show cards or muck
	NEW_HAND("NEW_HAND"), // new hand has begun
	NEW_TURN("NEW_TURN"); // indicates that a new players turn has begun
	
	private final String value;

	ActionType(String value) {
		this.value = value;
	}

	public String toValue() {
		return value;
	}
	
	public static ActionType fromValue(String value) {  
		if (value != null) {  
			for (ActionType type : values()) {  
				if (type.value.equals(value)) {  
					return type;  
				}  
			}  
		}  

		throw new IllegalArgumentException("Invalid action type: " + value);  
	}
}
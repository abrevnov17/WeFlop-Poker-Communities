package com.weflop.Game;

/**
 * Enum describes reasons to boot a player.
 * 
 * @author abrevnov
 *
 */
public enum BootReason {
	INSUFFICIENT_FUNDS("INSUFFICIENT_FUNDS"), // player does not have enough money to continue playing
	MISCONDUCT("MISCONDUCT"), // player has been abusing the chat or something of that nature
	FORBIDDEN_ACTIVITY("FORBIDDEN_ACTIVITY"), // client is sending unexpected messages that indicate a broken client or cheating
	INACTIVITY("INACTIVITY");

	private final String value;

	BootReason(String value) {
		this.value = value;
	}

	public String toValue() {
		return value;
	}
	
	public static BootReason fromValue(String value) {  
		if (value != null) {  
			for (BootReason reason : values()) {  
				if (reason.value.equals(value)) {  
					return reason;  
				}  
			}  
		}  

		throw new IllegalArgumentException("Invalid boot reason: " + value);  
	}
}

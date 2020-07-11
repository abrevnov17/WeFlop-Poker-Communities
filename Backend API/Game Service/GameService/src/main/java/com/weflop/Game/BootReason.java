package com.weflop.Game;

/**
 * Enum describes reasons to boot a player.
 * 
 * @author abrevnov
 *
 */
public enum BootReason {
	INSUFFICIENT_FUNDS(0), // player does not have enough money to continue playing
	MISCONDUCT(1), // player has been abusing the chat or something of that nature
	FORBIDDEN_ACTIVITY(2); // client is sending unexpected messages that indicate a broken client or
							// cheating

	private final int value;

	BootReason(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
}

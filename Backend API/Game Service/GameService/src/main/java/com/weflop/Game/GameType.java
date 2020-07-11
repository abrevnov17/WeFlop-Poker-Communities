package com.weflop.Game;

/**
 * Enum describes all supported game types.
 * 
 * @author abrevnov
 *
 */
public enum GameType {
	STANDARD_REPRESENTATION(0);

	private final int value;

	GameType(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
}

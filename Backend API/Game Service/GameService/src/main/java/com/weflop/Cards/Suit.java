package com.weflop.Cards;

/**
 * Suit.java
 * 
 * Enum describes all possible suits in standard 52-card deck.
 * 
 * @author abrevnov
 *
 */
public enum Suit {
	HEARTS(0), CLUBS(1), SPADES(2), DIAMONDS(3);

	private final int value;

	Suit(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
}

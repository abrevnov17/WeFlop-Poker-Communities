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
	HEARTS("HEARTS"), CLUBS("CLUBS"), SPADES("SPADES"), DIAMONDS("DIAMONDS");

	private final String value;

	Suit(String value) {
		this.value = value;
	}

	public String toValue() {
		return value;
	}

	public static Suit fromValue(String value) {  
		if (value != null) {  
			for (Suit suit : values()) {  
				if (suit.value.equals(value)) {  
					return suit;  
				}  
			}  
		}
		
		throw new IllegalArgumentException("Invalid suit: " + value);  
	}
}

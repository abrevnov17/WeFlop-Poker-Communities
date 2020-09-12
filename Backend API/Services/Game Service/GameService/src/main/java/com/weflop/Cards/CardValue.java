package com.weflop.Cards;

/**
 * CardValue.java
 * 
 * Enum describes all possible values of cards in standard 52-card deck.
 * 
 * @author abrevnov
 *
 */
public enum CardValue {
	TWO("TWO"), 
	THREE("THREE"), 
	FOUR("FOUR"), 
	FIVE("FIVE"), 
	SIX("SIX"), 
	SEVEN("SEVEN"), 
	EIGHT("EIGHT"), 
	NINE("NINE"), 
	TEN("TEN"), 
	JACK("JACK"), 
	QUEEN("QUEEN"), 
	KING("KING"), 
	ACE("ACE");

	private final String value;

	CardValue(String value) {
		this.value = value;
	}

	public String toValue() {
		return value;
	}
	
	public static CardValue fromValue(String value) {  
		if (value != null) {  
			for (CardValue card : values()) {  
				if (card.value.equals(value)) {  
					return card;  
				}  
			}  
		}  

		throw new IllegalArgumentException("Invalid card: " + value);  
	}
}

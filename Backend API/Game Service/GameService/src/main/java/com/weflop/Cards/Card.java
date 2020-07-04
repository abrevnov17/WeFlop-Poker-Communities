package com.weflop.Cards;

/**
 * Card.java
 * 
 * A Card instance represents a card from a standard
 * 52-card deck.
 * 
 * @author abrevnov
 *
 */
public class Card {

	private final Suit suit;

	private final CardValue value;

	public Card(Suit suit, CardValue value) {
		this.value = value;
		this.suit = suit;
	}

	public Suit getSuit() {
		return this.suit;
	}

	public CardValue getCardValue() {
		return this.value;
	}
}

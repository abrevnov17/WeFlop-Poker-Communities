package com.weflop.GameService.Database.DomainObjects;

/**
 * CRUD object wraps information about cards
 * 
 * @author abrevnov
 *
 */
public class CardPOJO {
	private int suit;

	private int value;

	public CardPOJO(int suit, int value) {
		super();
		this.suit = suit;
		this.value = value;
	}

	public int getSuit() {
		return suit;
	}

	public void setSuit(int suit) {
		this.suit = suit;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
}

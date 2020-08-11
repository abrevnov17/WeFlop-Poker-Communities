package com.weflop.GameService.Database.DomainObjects;

/**
 * CRUD object wraps information about cards
 * 
 * @author abrevnov
 *
 */
public class CardPOJO {
	private String suit;

	private String value;

	public CardPOJO(String suit, String value) {
		super();
		this.suit = suit;
		this.value = value;
	}

	public String getSuit() {
		return suit;
	}

	public void setSuit(String suit) {
		this.suit = suit;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}

package com.weflop.Cards;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.weflop.GameService.Database.DomainObjects.CardPOJO;

/**
 * Center cards for a table.
 * 
 * @author abrevnov
 */
public class Board {
	private List<Card> cards;
	
	public Board() {
		cards = new ArrayList<Card>();
	}
	
	public Board(List<Card> cards) {
		this.setCards(cards);
	}
	
	public void addCard(Card card) {
		this.cards.add(card);
	}
	
	public void discard() {
		this.cards.clear();
	}
	
	public List<CardPOJO> toPOJO() {
		return this.cards.stream()
				.map(card -> new CardPOJO(card.getSuit().toValue(), card.getCardValue().toValue()))
				.collect(Collectors.toList());
	}

	/* Getters and Setters */
	
	public List<Card> getCards() {
		return cards;
	}

	public void setCards(List<Card> cards) {
		this.cards = cards;
	}
	
	
}

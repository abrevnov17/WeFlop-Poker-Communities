package com.weflop.Cards;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.weflop.Evaluation.HandRank;
import com.weflop.GameService.Database.DomainObjects.CardPOJO;

/**
 * A player hand.
 * 
 * @author abrevnov
 *
 */
public class Hand implements Comparable<Hand> {
	private List<Card> cards;
	private HandRank rank;

	public Hand() {
		this.setCards(new ArrayList<Card>());
	}
	
	public Hand(List<Card> cards) {
		this.setCards(cards);
	}

	public void addCardToHand(Card card) {
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
	
	public static Hand fromPOJO(List<CardPOJO> handPojo) {
		List<Card> cards = handPojo.stream()
				.map(card -> new Card(Suit.fromValue(card.getSuit()), CardValue.fromValue(card.getValue())))
				.collect(Collectors.toList());
		
		return new Hand(cards);
	}

	/**
	 * Compares the strength of the hand.
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public final int compareTo(Hand hand) {
		return rank.compareTo(hand.getRank());
	}

	/* Getters and Setters */

	public List<Card> getCards() {
		return cards;
	}

	public void setCards(List<Card> cards) {
		this.cards = cards;
	}

	public HandRank getRank() {
		return rank;
	}

	public void setRank(HandRank rank) {
		this.rank = rank;
	}


}

package com.weflop.Game;

import java.util.ArrayList;
import java.util.List;

import com.weflop.Cards.Card;

/**
 * Player.java
 * 
 * Encapsulates information pertaining to player in a specific game.
 * 
 * @author abrevnov
 *
 */
public class Player {
	private final long id;
	private List<Card> cards;
	private float balance; 
	private float currentBet;
	private PlayerState state;
	
	Player(long id) {
		this.id = id;
		this.setCards(new ArrayList<Card>());
		this.setBalance(0.00f);
		this.setCurrentBet(0.00f);
		this.setState(PlayerState.WATCHING);
	}
	
	Player(long id, List<Card> cards) {
		this.id = id;
		this.setCards(cards);
		this.setBalance(0.00f);
		this.setCurrentBet(0.00f);
		this.setState(PlayerState.WATCHING);		
	}

	public long getId() {
		return id;
	}

	synchronized public List<Card> getCards() {
		return cards;
	}

	synchronized public void setCards(List<Card> cards) {
		this.cards = cards;
	}

	synchronized public float getBalance() {
		return balance;
	}

	synchronized public void setBalance(float balance) {
		this.balance = balance;
	}

	synchronized public float getCurrentBet() {
		return currentBet;
	}

	public void setCurrentBet(float currentBet) {
		this.currentBet = currentBet;
	}

	synchronized public PlayerState getState() {
		return state;
	}

	synchronized public void setState(PlayerState state) {
		this.state = state;
	}
	
	synchronized public void addCard(Card card) {
		this.cards.add(card);
	}
}

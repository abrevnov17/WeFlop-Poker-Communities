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
	private long id;
	private List<Card> cards;
	private float balance; 
	private float currentBet;
	private PlayerState state;
	
	Player(long id) {
		this.setId(id);
		this.setCards(new ArrayList<Card>());
		this.setBalance(0.00f);
		this.setCurrentBet(0.00f);
		this.setState(PlayerState.WATCHING);
	}
	
	Player(long id, List<Card> cards) {
		this.setId(id);
		this.setCards(cards);
		this.setBalance(0.00f);
		this.setCurrentBet(0.00f);
		this.setState(PlayerState.WATCHING);		
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public List<Card> getCards() {
		return cards;
	}

	public void setCards(List<Card> cards) {
		this.cards = cards;
	}

	public float getBalance() {
		return balance;
	}

	public void setBalance(float balance) {
		this.balance = balance;
	}

	public float getCurrentBet() {
		return currentBet;
	}

	public void setCurrentBet(float currentBet) {
		this.currentBet = currentBet;
	}

	public PlayerState getState() {
		return state;
	}

	public void setState(PlayerState state) {
		this.state = state;
	}
}

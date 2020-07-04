package com.weflop.Database.DomainObjects;

import java.util.List;

/**
 * CRUD object wrapping player information.
 * 
 * @author abrevnov
 *
 */
public class PlayerPOJO {

	private String id;
	
	private float balance;
	
	private float currentBet;

	private List<CardPOJO> cards;
	
	private int state;
	
	public PlayerPOJO(String id, float balance, float currentBet, List<CardPOJO> cards, int state) {
		this.id = id;
		this.balance = balance;
		this.currentBet = currentBet;
		this.cards = cards;
		this.state = state;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public List<CardPOJO> getCards() {
		return cards;
	}

	public void setCards(List<CardPOJO> cards) {
		this.cards = cards;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}
}

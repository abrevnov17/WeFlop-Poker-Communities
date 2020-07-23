package com.weflop.GameService.Database.DomainObjects;

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
	
	private float currentRoundBet;

	private List<CardPOJO> cards;

	private int state;

	private int slot;

	public PlayerPOJO(String id, float balance, float currentBet, float currentRoundBet, List<CardPOJO> cards, int state, int slot) {
		this.id = id;
		this.balance = balance;
		this.currentBet = currentBet;
		this.setCurrentRoundBet(currentRoundBet);
		this.cards = cards;
		this.state = state;
		this.slot = slot;
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

	public int getSlot() {
		return slot;
	}

	public void setSlot(int slot) {
		this.slot = slot;
	}

	public float getCurrentRoundBet() {
		return currentRoundBet;
	}

	public void setCurrentRoundBet(float currentRoundBet) {
		this.currentRoundBet = currentRoundBet;
	}
}

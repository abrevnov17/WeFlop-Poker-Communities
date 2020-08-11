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

	private String state;
	
	private String nextHandState;
	
	private String prevState;

	private int slot;

	public PlayerPOJO(String id, float balance, float currentBet, float currentRoundBet, List<CardPOJO> cards, 
			String state, String nextHandState, String prevState, int slot) {
		this.id = id;
		this.balance = balance;
		this.currentBet = currentBet;
		this.currentRoundBet = currentRoundBet;
		this.cards = cards;
		this.state = state;
		this.nextHandState = nextHandState;
		this.slot = slot;
		this.setPrevState(prevState);
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

	public String getState() {
		return state;
	}

	public void setState(String state) {
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

	public String getNextHandState() {
		return nextHandState;
	}

	public void setNextHandState(String nextHandState) {
		this.nextHandState = nextHandState;
	}

	public String getPrevState() {
		return prevState;
	}

	public void setPrevState(String prevState) {
		this.prevState = prevState;
	}
}

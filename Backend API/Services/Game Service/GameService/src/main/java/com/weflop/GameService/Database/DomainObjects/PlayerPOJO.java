package com.weflop.GameService.Database.DomainObjects;

import com.weflop.GameService.Game.PlayerSettings;

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
	
	private float handBalance;

	private float handBet;
	
	private float roundBet;

	private List<CardPOJO> cards;

	private String state;
	
	private String nextHandState;
	
	private String prevState;

	private int slot;

	private PlayerSettings settings;

	public PlayerPOJO(String id, float balance, float handBalance, float handBet, float roundBet, List<CardPOJO> cards, 
			String state, String nextHandState, String prevState, int slot, PlayerSettings settings) {
		this.id = id;
		this.balance = balance;
		this.handBalance = handBalance;
		this.handBet = handBet;
		this.roundBet = roundBet;;
		this.cards = cards;
		this.state = state;
		this.nextHandState = nextHandState;
		this.slot = slot;
		this.prevState = prevState;
		this.settings = settings;
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

	public float getHandBet() {
		return handBet;
	}

	public void setHandBet(float handBet) {
		this.handBet = handBet;
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

	public float getRoundBet() {
		return roundBet;
	}

	public void setRoundBet(float roundBet) {
		this.roundBet = roundBet;
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

	public float getHandBalance() {
		return handBalance;
	}

	public void setHandBalance(float handBalance) {
		this.handBalance = handBalance;
	}

	public PlayerSettings getSettings() {
		return settings;
	}

	public void setSettings(PlayerSettings settings) {
		this.settings = settings;
	}
}

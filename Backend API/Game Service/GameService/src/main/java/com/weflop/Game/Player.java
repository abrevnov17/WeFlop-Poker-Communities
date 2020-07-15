package com.weflop.Game;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.util.Assert;
import org.springframework.web.socket.WebSocketSession;

import com.weflop.Cards.Card;
import com.weflop.GameService.Database.DomainObjects.CardPOJO;
import com.weflop.GameService.Database.DomainObjects.PlayerPOJO;
import com.weflop.GameService.Database.DomainObjects.SpectatorPOJO;

/**
 * Player.java
 * 
 * Encapsulates information pertaining to player in a specific game.
 * 
 * @author abrevnov
 *
 */
public class Player {
	private final String id;
	private List<Card> cards;
	private float balance;
	private float currentBet;
	private PlayerState state;
	int slot; // position of player in table (clockwise increasing, -1 if spectator)

	private WebSocketSession session;

	Player(String id, WebSocketSession session) {
		this(id, session, new ArrayList<Card>());
	}

	Player(String id, WebSocketSession session, List<Card> cards) {
		this.id = id;
		this.setSession(session);
		this.setCards(cards);
		this.setBalance(0.00f);
		this.setCurrentBet(0.00f);
		this.setState(PlayerState.WATCHING);
		this.setSlot(-1);
	}

	/**
	 * Returns whether the player has a sufficient balance to place a given bet.
	 * 
	 * @param amount
	 *            Amount to bet
	 * @return Whether or not the player has a sufficient balance
	 */
	synchronized private boolean canBet(float amount) {
		if (this.balance >= amount) {
			return true;
		}

		return false;
	}

	/**
	 * Reduces balance by bet amount;
	 * 
	 * @param amount
	 * @return
	 */
	synchronized public void bet(float amount) throws IllegalStateException {
		Assert.isTrue(this.canBet(amount), "Insufficient funds to place bet");
		this.balance -= amount;
		this.currentBet += amount;
	}

	/**
	 * Reduces balance to zero and switches state to all in.
	 * 
	 * @return The balance they forfeited to the pot to go all in
	 */
	synchronized float goAllIn() {
		float currBalance = this.balance;

		this.balance = 0.0f;
		this.state = PlayerState.ALL_IN;

		this.currentBet += currBalance;

		return currBalance;
	}

	synchronized void convertToSpectator() {
		this.cards.clear();
		this.currentBet = 0.0f;
		this.state = PlayerState.WATCHING;
		this.slot = -1;
	}

	/**
	 * Discards all player cards.
	 * 
	 */
	synchronized public void discardHand() {
		this.cards.clear();
	}

	/**
	 * Increased balance by a given amount.
	 * 
	 * @param amount
	 *            Amount to add to balance
	 */
	synchronized public void increaseBalance(float amount) {
		this.balance += amount;
	}

	/**
	 * Converts Player instance to Player POJO.
	 * 
	 * @return Corresponding instance of PlayerPOJO
	 */
	synchronized public PlayerPOJO toPlayerPOJO() {
		List<CardPOJO> cards = this.cards.stream()
				.map(card -> new CardPOJO(card.getSuit().getValue(), card.getCardValue().getValue()))
				.collect(Collectors.toList());
		return new PlayerPOJO(this.id, this.balance, this.currentBet, cards, this.state.getValue(), this.slot);
	}

	/**
	 * Converts Player instance to Spectator POJO.
	 * 
	 * @return Corresponding instance of SpectatorPOJO
	 */
	synchronized public SpectatorPOJO toSpectatorPOJO() {
		return new SpectatorPOJO(this.id);
	}

	/* Overriding default object methods */

	@Override
	public boolean equals(Object o) {

		// If the object is compared with itself then return true
		if (o == this) {
			return true;
		}

		/* Check if o is an instance of Player */
		if (!(o instanceof Player)) {
			return false;
		}

		// cast o to Player so that we can compare data members
		Player p = (Player) o;

		// Compare the data members and return accordingly
		return id == p.id;
	}

	/* Getters and Setters */

	public String getId() {
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

	synchronized public boolean isPlaying() {
		return this.state != PlayerState.WATCHING;
	}

	synchronized public boolean isSpectating() {
		return this.state == PlayerState.WATCHING;
	}

	synchronized public WebSocketSession getSession() {
		return session;
	}

	synchronized public void setSession(WebSocketSession session) {
		this.session = session;
	}

	public int getSlot() {
		return slot;
	}

	public void setSlot(int slot) {
		this.slot = slot;
	}
}

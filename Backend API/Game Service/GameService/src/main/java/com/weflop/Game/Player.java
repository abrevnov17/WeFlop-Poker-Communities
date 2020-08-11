package com.weflop.Game;

import org.springframework.util.Assert;
import org.springframework.web.socket.WebSocketSession;

import com.weflop.Cards.Card;
import com.weflop.Cards.Hand;
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
	private Hand hand;
	private float balance;
	private float currentBet;
	private float currentRoundBet;
	
	private PlayerState state;
	private PlayerState nextHandState; // state player will transition to at start of next hand
	private PlayerState prevState; // prior state that player transitioned from
	
	private int slot; // position of player in table (clockwise increasing, -1 if spectator)
	
	private boolean displayingInactivity; // true if last action was turn expiration

	private WebSocketSession session;

	Player(String id, WebSocketSession session) {
		this(id, session, new Hand());
	}

	Player(String id, WebSocketSession session, Hand hand) {
		this.id = id;
		this.setSession(session);
		this.setHand(hand);
		this.setBalance(0.00f);
		this.setCurrentBet(0.00f);
		this.setCurrentRoundBet(0.00f);
		this.setState(PlayerState.WATCHING);
		this.setNextHandState(PlayerState.WATCHING);
		this.setPrevState(PlayerState.WATCHING);
		this.setDisplayingInactivity(false);
		
		this.setSlot(-1);
	}
	
	Player(String id, WebSocketSession session, Hand hand, float balance, 
			float currentBet, float currentRoundBet, PlayerState state, 
			PlayerState nextHandState, PlayerState prevState, int slot) {
		this.id = id;
		this.setSession(session);
		this.setHand(hand);
		this.setBalance(balance);
		this.setCurrentBet(currentBet);
		this.setCurrentRoundBet(currentRoundBet);
		this.setState(state);
		this.setNextHandState(nextHandState);
		this.setPrevState(prevState);
		this.setSlot(slot);
	}

	/**
	 * Returns whether the player has a sufficient balance to place a given bet.
	 * Note: Players cannot place bets that make them go all in. That is done only
	 * through the ALL_IN action.
	 * 
	 * @param amount
	 *            Amount to bet
	 * @return Whether or not the player has a sufficient balance
	 */
	synchronized private boolean canBet(float amount) {
		if (this.balance - amount >= 0.01) {
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
		this.currentRoundBet += amount;
	}

	/**
	 * Reduces balance to zero and switches state to all in.
	 * 
	 * @return The balance they forfeited to the pot to go all in
	 */
	synchronized public float goAllIn() {
		float currBalance = this.balance;

		this.balance = 0.0f;
		updateCurrentAndFutureState(PlayerState.ALL_IN, PlayerState.WAITING_FOR_TURN);

		this.currentBet += currBalance;
		this.currentRoundBet += currBalance;

		return currBalance;
	}

	synchronized void convertToSpectator() {
		this.discardHand();
		this.currentBet = 0.00f;
		this.state = PlayerState.WATCHING;
		this.currentRoundBet = 0.00f;
		this.nextHandState = PlayerState.WATCHING;
		this.prevState = PlayerState.WATCHING;
		this.slot = -1;
	}
	
	synchronized public void discardHand() {
		this.hand.discard();
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
	synchronized public PlayerPOJO toPOJO() {
		return new PlayerPOJO(this.id, this.balance, this.currentBet, this.currentRoundBet,
				hand.toPOJO(), this.state.toValue(), this.nextHandState.toValue(), this.prevState.toValue(), this.slot);
	}
	
	public static Player fromPOJO(PlayerPOJO pojo) {
		return new Player(pojo.getId(), null, Hand.fromPOJO(pojo.getCards()), pojo.getBalance(), 
				pojo.getCurrentBet(), pojo.getCurrentRoundBet(), PlayerState.fromValue(pojo.getState()), 
				PlayerState.fromValue(pojo.getNextHandState()), 
				PlayerState.fromValue(pojo.getPrevState()), pojo.getSlot());
	}
	
	public static Player fromSpectatorPOJO(SpectatorPOJO pojo) {
		return null;
	}
	
	/**
	 * Converts player from spectator to player waiting for big blind.
	 * 
	 * @param Seat player is sitting at.
	 */
	synchronized public void sit(int slot) {
		Assert.isTrue(this.isSpectating(), "Must be spectator to sit");
		
		this.setSlot(slot);

		// player waits for big blind to reach them before being able to play a hand
		updateStates(PlayerState.WATCHING, PlayerState.WAITING_FOR_BIG_BLIND, PlayerState.WAITING_FOR_BIG_BLIND);
	}
	
	synchronized public boolean canSitOut() {
		return this.isActive();
	}
	
	synchronized public boolean canChangeSeat() {
		return !this.isActive() && !this.isSpectating();
	}
	
	synchronized public boolean canPostBigBlind(float bigBlind) {
		return this.state == PlayerState.WAITING_FOR_BIG_BLIND && balance >= bigBlind;
	}
	
	/**
	 * Returns whether player can be blind. Note that player who is waiting for a big blind
	 * is eligible to be any blind, big or small (we do not want to skip over players waiting for big blind
	 * in edge cases where players who were supposed to be small blind leave or sit out).
	 * @return true if player can be a blind during the current hand, false otherwise.
	 */
	synchronized public boolean canBeBlind() {
		return this.isActive() || isWaitingForBigBlind();
	}
	
	/**
	 * Method returns true if player is waiting for big blind (either they are sitting out or just joined).
	 * Returns false otherwise.
	 */
	synchronized public boolean isWaitingForBigBlind() {
		return this.state == PlayerState.WAITING_FOR_BIG_BLIND || this.state == PlayerState.SITTING_OUT_BB;
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
		return id.equals(p.id);
	}
	
	synchronized public void addCard(Card card) {
		this.hand.addCardToHand(card);
	}

	synchronized public boolean isSpectating() {
		return this.state == PlayerState.WATCHING;
	}
	
	/**
	 * Returns whether player is active in round
	 * and has not folded.
	 * @return
	 */
	synchronized public boolean isActiveInBettingRound() {
		return isActive() && state != PlayerState.FOLDED;
	}
	
	synchronized public boolean isActive() {
		return !isSpectating() 
				&& state != PlayerState.WAITING_FOR_HAND
				&& state != PlayerState.SITTING_OUT_BB
				&& state != PlayerState.WAITING_FOR_BIG_BLIND
				&& state != PlayerState.POSTING_BIG_BLIND;
	}
	
	/**
	 * Returns true if a player is waiting for their turn (including
	 * if they have checked or bet already in round but are still eligible to play).
	 */
	synchronized public boolean canMoveInRound() {
		return this.state == PlayerState.WAITING_FOR_TURN || this.state == PlayerState.CHECKED;
	}
	
	synchronized public void transitionState() {
		this.state = nextHandState;
	}

	/* Getters and Setters */

	public String getId() {
		return id;
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
		this.prevState = this.state; // updating previous state
		this.state = state;
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

	synchronized public float getCurrentRoundBet() {
		return currentRoundBet;
	}

	synchronized public void setCurrentRoundBet(float currentRoundBet) {
		this.currentRoundBet = currentRoundBet;
	}

	synchronized public Hand getHand() {
		return hand;
	}

	synchronized public void setHand(Hand hand) {
		this.hand = hand;
	}

	synchronized public PlayerState getNextHandState() {
		return nextHandState;
	}

	synchronized public void setNextHandState(PlayerState nextHandState) {
		this.nextHandState = nextHandState;
	}
	
	synchronized public void updateCurrentAndFutureState(PlayerState currentState, PlayerState nextHandState) {
		setState(currentState);
		setNextHandState(nextHandState);
	}
	
	synchronized public void updateStates(PlayerState prevState, PlayerState currentState, PlayerState nextState) {
		this.prevState = prevState;
		this.state = currentState;
		this.nextHandState = nextState;
	}

	public PlayerState getPrevState() {
		return prevState;
	}

	public void setPrevState(PlayerState prevState) {
		this.prevState = prevState;
	}

	public boolean isDisplayingInactivity() {
		return displayingInactivity;
	}

	public void setDisplayingInactivity(boolean displayingInactivity) {
		this.displayingInactivity = displayingInactivity;
	}
}

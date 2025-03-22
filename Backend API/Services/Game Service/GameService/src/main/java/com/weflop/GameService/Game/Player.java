package com.weflop.GameService.Game;

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
	
	private float balance; // player total balance
	private float handBalance; // balance available during hand
	
	private float roundBet; // current bet for round
	private float handBet; // current bet for hand
	
	private boolean missedBlind;
	private PlayerState state;
	private PlayerState nextHandState; // state player will transition to at start of next hand
	private PlayerState prevState; // prior state that player transitioned from
	
	private int slot; // position of player in table (clockwise increasing, -1 if spectator)
	
	private boolean displayingInactivity; // true if last action was turn expiration
	
	private PlayerSettings settings; // customizable player settings (e.x. auto-mucking)

	private WebSocketSession session;

	Player(String id, WebSocketSession session, Hand hand) {
		this.id = id;
		this.setSession(session);
		this.setHand(hand);
		this.setBalance(0.00f);
		this.setHandBalance(0.00f);
		this.setHandBet(0.00f);
		this.setRoundBet(0.00f);
		this.setState(PlayerState.WATCHING);
		this.setNextHandState(PlayerState.WATCHING);
		this.setPrevState(PlayerState.WATCHING);
		this.setDisplayingInactivity(false);		
		this.setSlot(-1);
		this.setMissedBlind(false);
		this.setSettings(new PlayerSettings()); // loading default player settings
	}
	
	Player(String id, WebSocketSession session, Hand hand, float balance, 
			float handBet, float roundBet, PlayerState state, 
			PlayerState nextHandState, PlayerState prevState, int slot) {
		this(id, session, hand);
		this.setBalance(balance);
		this.setHandBet(handBet);
		this.setRoundBet(roundBet);
		this.setState(state);
		this.setNextHandState(nextHandState);
		this.setPrevState(prevState);
		this.setSlot(slot);
	}
	
	Player(String id, WebSocketSession session) {
		this(id, session, new Hand());
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
		if (this.handBalance - amount >= 0.01) {
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
		Assert.isTrue(amount > 0,"Cannot bet 0, must check");
		this.balance -= amount;
		this.handBalance -= amount;
		this.handBet += amount;
		this.roundBet += amount;
		System.out.printf("This: %f, Round: %f", amount, this.handBet);
	}

	/**
	 * Reduces balance to zero and switches state to all in.
	 * 
	 * @return The balance they forfeited to the pot to go all in
	 */
	synchronized public float goAllIn() {
		float currHandBalance = this.handBalance;

		this.balance = this.balance - currHandBalance;
		this.handBalance = 0.00f;
		
		updateCurrentAndFutureState(PlayerState.ALL_IN, PlayerState.WAITING_FOR_TURN);

		this.handBet += currHandBalance;
		this.roundBet += currHandBalance;

		return currHandBalance;
	}

	synchronized void convertToSpectator() {
		this.discardHand();
		this.handBet = 0.00f;
		this.setState(PlayerState.WATCHING);
		this.roundBet = 0.00f;
		this.setNextHandState(PlayerState.WATCHING);
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
		return new PlayerPOJO(this.id, this.balance, this.handBalance, this.handBet, this.roundBet,
				hand.toPOJO(), this.state.toValue(), this.nextHandState.toValue(),
				this.prevState.toValue(), this.slot, this.settings);
	}
	
	public static Player fromPOJO(PlayerPOJO pojo) {
		return new Player(pojo.getId(), null, Hand.fromPOJO(pojo.getCards()), pojo.getBalance(), 
				pojo.getHandBet(), pojo.getRoundBet(), PlayerState.fromValue(pojo.getState()), 
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
		return this.isPlaying() || isWaitingForBigBlind();
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
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Player other = (Player) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
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
				&& state != PlayerState.WAITING_FOR_BIG_BLIND
				&& state != PlayerState.POSTING_BIG_BLIND
				&& state != PlayerState.SITTING_OUT
				&& state != PlayerState.BUSTED;
	}
	
	synchronized public boolean isPlaying() {
		return !isSpectating() 
  				&& state != PlayerState.POSTING_BIG_BLIND
				&& state != PlayerState.SITTING_OUT
				&& state != PlayerState.BUSTED;	
		
	}
	
	synchronized public boolean hasFolded() {
		return state == PlayerState.FOLDED;
	}
	
	/**
	 * Returns true if a player is waiting for their turn (including
	 * if they have checked or bet already in round but are still eligible to play).
	 */
	synchronized public boolean canMoveInRound() {
		return this.state == PlayerState.WAITING_FOR_TURN 
				|| this.state == PlayerState.CHECKED
				|| this.state == PlayerState.AUTO_CALL
				|| this.state == PlayerState.AUTO_CHECK_OR_FOLD;
	}
	
	synchronized public void transitionState() {
		System.out.print("\n STATE ");
		System.out.print(state);
		System.out.print(" ");
		System.out.print(nextHandState);
		System.out.print(" ENDSTATE \n");
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

	synchronized public float getHandBet() {
		return handBet;
	}

	public void setHandBet(float handBet) {
		this.handBet = handBet;
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

	synchronized public float getRoundBet() {
		return roundBet;
	}

	synchronized public void setRoundBet(float roundBet) {
		this.roundBet = roundBet;
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
	synchronized public void setMissedBlind(boolean missed) {
		this.missedBlind = missed;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
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
	public boolean getMissedBlind() {
		return this.missedBlind;
	}

}

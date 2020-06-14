package com.weflop.Game;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.weflop.Cards.Card;
import com.weflop.Evaluation.HandRank;
import com.weflop.Evaluation.HandRankEvaluator;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 
 * Abstract class defining some shared properties and methods as well
 * as some required abstract methods for all instances of games.
 * 
 * @author abrevnov
 *
 */
public abstract class AbstractGame implements Game {
	
	private final UUID id;
	
	private Lock lock; // manages concurrent read/write access to game properties

	private boolean started;
	private Instant startTime;
	
	private float pot;

	private List<Card> centerCards;
	private int dealerIndex;
	
	private float smallBlind;
	private float bigBlind; // almost always just 2x the smallBlind
	
	private float roundBet;
	
	private Group group; // our group of players

	private Turn turn;
	
	private Duration turnDuration; // how long user gets per turn
	
	private HandRankEvaluator evaluator;
		
	protected AbstractGame(float smallBlind, int tableSize, Duration turnDuration, HandRankEvaluator evaluator) {
		this.id = UUID.randomUUID();
		this.setPot(0.0f);
		this.centerCards = new ArrayList<Card>();
		this.dealerIndex = 0;
		this.setStartTime(null); // do not start clock till start() called
		this.smallBlind = smallBlind;
		this.bigBlind = 2.0f*smallBlind;
		this.setGroup(new Group(tableSize));
		this.turn = null; // is not initialized until game starts
		this.setStarted(false);
		this.setLock(new ReentrantLock());
		this.setTurnDuration(turnDuration);
		this.setRoundBet(this.bigBlind);
		this.evaluator = evaluator;
	}
	
	// if the big blind is not just 2x small blind
	protected AbstractGame(float smallBlind, float bigBlind, int tableSize, Duration turnDuration, HandRankEvaluator evaluator) {
		this(smallBlind, tableSize, turnDuration, evaluator);
		this.bigBlind = bigBlind;
		this.setRoundBet(this.bigBlind);
	}
	
	/* These methods are publicly exposed and will be overriden by subclasses: */
	
	public abstract void start() throws Exception; // starts a game
	public abstract void end(); // cleanly ends a game AND flushes to database
	public abstract void performAction(long participantID, Action action) throws Exception; // performs an action as a given participant
	public abstract void sendGamePackets() throws Exception; // sends game packets (i.e. copies of game state) to each player
	public abstract void flushToDatabase() throws Exception; // flushes game state to database
	
	/* Required methods (internally used) for all subclasses */
	protected abstract void deal(); // deals cards to players
	
	/**
	 * Handler for turn expirations. Called by a single thread after execution.
	 * 
	 */
	synchronized public void turnExpired(int turnCount) {
		// check to see if provided turn count matches current turn
		if (turnCount != this.turn.getCount()) {
			return; // this call is being applied to a stale turn
		}
		
		// otherwise, we know that a user has failed to perform an action in the alloted
		// time. in such cases, the user performs a TURN_TIMEOUT action, which (will likely
		// be handled in a similar manner as to a fold)
		try {
			this.performAction(this.turn.getPlayer().getId(), new Action(ActionType.TURN_TIMEOUT));
		} catch (Exception e) {
			// we do not need to do anything. this is only possible in an incredibly unlikely
			// race condition where the current players action is processed between the last if statement
			// and the current call to perform the action of a turn timeout. In this case, we can just
			// ignore the exception and consider this turn to be stale.
		}
	}
	
	/* Universally shared methods */
	
	/**
	 * Spawns a timer-thread that will send game packets when a turn has expired
	 */
	protected void beginTurnTimer() {
		ScheduledExecutorService turnTimer = Executors
		        .newSingleThreadScheduledExecutor();

		    Runnable packetSender = new Runnable() {
		      @Override
		      public void run() {
		        try {
		          sendGamePackets();
		        } catch (Exception e) {
		          e.printStackTrace();
		        }
		      }
		    };

		    // resent packets after the duration of the turn has passed
		    turnTimer.schedule(packetSender, this.turnDuration.getSeconds(), TimeUnit.SECONDS);
	}
	
	/**
	 * Helper function to begin a round. Note that any locking must be handled
	 * outside of this function as this function is not inherently thread-safe.
	 * 
	 * Additionally, beginRound() expects an up-to-date dealer index and that all
	 * players have sufficient funds to play the current round.
	 * 
	 */
	protected void beginRound() {
		// first we deal cards
		this.deal();
		
		// Note: We do not need to check if the small/big blind can pay or not because that
		// is done at the end of each round
				
		// small blind pays
		this.getSmallBlindPlayer().bet(this.smallBlind);
		this.pot += this.smallBlind;
		
		// big blind pays
		this.getBigBlindPlayer().bet(this.bigBlind);
		this.pot += this.bigBlind;
		
		// resetting the current round bet amount (this is the most any player has bet during
		// the current round)
		this.setRoundBet(this.getBigBlind());
		
		// cycling to next turn
		cycleTurn(this.getBigBlindIndex());
	}
	
	/**
	 * Helper function to end a round. Updates dealer index and checks to see that all players
	 * have sufficient funds to player the current round.
	 * 
	 */
	protected void endRound() {
		
		// calculate winning hand(s)
		List<Player> playersWithMaxRank = new ArrayList<Player>();
		HandRank maxRank = null;
		for (Player player : this.group.getPlayers()) {
			if (player.getState() != PlayerState.FOLDED) {
				HandRank rank = this.evaluator.evaluate(this.centerCards, player.getCards());
				if (maxRank == null || rank.compareTo(maxRank) == 0) {
					// either first handrank we have found or tied with best hand rank we have found
					maxRank = rank;
					playersWithMaxRank.add(player);
				} else if (rank.compareTo(maxRank) < 0) {
					// hand rank is best we have found so far
					playersWithMaxRank.clear();
					maxRank = rank;
					playersWithMaxRank.add(player);
				}
			}
		}
		
		// distribute funds to winner(s)
		float perPlayerWinnings = this.pot / playersWithMaxRank.size(); // split pot between winners
		for (Player player : playersWithMaxRank) {
			player.increaseBalance(perPlayerWinnings);
		}
		
		// removing any players with insufficient funds
		for (Player player : this.group.getPlayers()) {
			if (player.getBalance() < this.bigBlind) {
				this.bootPlayer(player, BootReason.INSUFFICIENT_FUNDS);
			}
		}
		
		// checking to see if we have enough players to continue
		if (this.group.getPlayers().size() < 2) {
			this.end();
		}
		
		// update dealer index
		this.dealerIndex = this.getNextPlayerIndex(this.dealerIndex);
		
		// starting next round
		this.beginRound();
	}
	
	protected void bootPlayer(Player player, BootReason reason) {
		this.group.movePlayerToSpectator(player);
		switch(reason) {
			case INSUFFICIENT_FUNDS:
				// TODO: send message
				break;
			case MISCONDUCT:
				// TODO: send message
				break;
			case FORBIDDEN_ACTIVITY:
				// TODO: send message
				break;
			default:
				// TODO: send message
				break;
		}
	}
	
	/**
	 * Given an index of the player in the last turn (or the big blind index
	 * in the case that this is the start of a round), updates the turn to the
	 * next elligible player who is not all-in.
	 * 
	 * Also, this function checks to see if the round has ended and calls the
	 * appropriate handler for that case.
	 * 
	 * @param lastTurnIndex
	 */
	protected void cycleTurn(int lastTurnIndex) {
		// checking to see if the round is over
		if (isRoundOver()) {
			this.endRound();
		}
		// setting current turn to be person
		int nextPlayerIndex = getNextPlayerIndex(lastTurnIndex);
		Player nextPlayer = this.group.getPlayers().get(nextPlayerIndex);
		
		while (nextPlayer.getState() == PlayerState.ALL_IN) {
			// this player is all-in, so we skip over them
			nextPlayerIndex++;
			nextPlayer = this.group.getPlayers().get(nextPlayerIndex);
		}
		
		this.turn = new Turn(getNextPlayer(lastTurnIndex), System.nanoTime());
		this.turn.getPlayer().setState(PlayerState.CURRENT_TURN);
		
		// starting the turn timer
		this.beginTurnTimer();
	}
	
	/**
	 * A round is over when every player has either folded, bet the 'roundBet' (i.e.
	 * bet as much as the most any player has bet that round), or has gone all in.
	 * 
	 * @return A boolean indicating if the round is over.
	 */
	protected boolean isRoundOver() {
		for (Player player : this.group.getPlayers()) {
			if (!(player.getState() == PlayerState.FOLDED || 
					player.getState() == PlayerState.ALL_IN 
					|| player.getCurrentBet() == this.roundBet)) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Checks that it is a given player's current turn.
	 * Throws an exception if it is not.
	 * 
	 * @param player
	 * @throws Exception
	 */
	synchronized protected void assertIsPlayerTurn(Player player) throws Exception {
		if (!this.getTurn().getPlayer().equals(player)) {
			throw new Exception("Player cannot perform that action "
					+ "as it is no longer their turn.");
		}
	}

	/* Getters and setters for universal game properties */
		
	/**
	 * Gets the index of the next player (clockwise) in our group
	 * of players given the index of the previous player in the group.
	 * 
	 * @param index Index of player whose turn is before the current player
	 * @return The index of the desired player
	 */
	synchronized protected int getNextPlayerIndex(int index) {
		return (index+1) % this.group.getPlayers().size();
	}
	
	synchronized protected Player getNextPlayer(int index) {
		return this.group.getPlayers().get(getNextPlayerIndex(index));
	}
	
	synchronized protected int getSmallBlindIndex() {
		return getNextPlayerIndex(dealerIndex+1);
	}
	
	synchronized protected Player getSmallBlindPlayer() {
		return this.group.getPlayers().get(getSmallBlindIndex());
	}
	
	synchronized protected int getBigBlindIndex() {
		return getNextPlayerIndex(dealerIndex+2);
	}
	
	synchronized protected Player getBigBlindPlayer() {
		return this.group.getPlayers().get(getBigBlindIndex());
	}
	
	synchronized protected Player getParticipantById(long id) throws Exception {
		// looping through players
		for (Player player : group.getPlayers()) {
			if (player.getId() == id) {
				return player;
			}
		}
		// looping through spectators
		for (Player spectator : group.getSpectators()) {
			if (spectator.getId() == id) {
				return spectator;
			}
		}
		
		throw new Exception("Invalid participant id");
	}
	
	protected UUID getId() {
		return id;
	}

	synchronized protected boolean isStarted() {
		return started;
	}

	synchronized protected void setStarted(boolean started) {
		this.started = started;
	}

	synchronized protected Instant getStartTime() {
		return startTime;
	}

	synchronized protected void setStartTime(Instant startTime) {
		this.startTime = startTime;
	}

	synchronized protected float getPot() {
		return pot;
	}

	synchronized protected void setPot(float pot) {
		this.pot = pot;
	}
	
	synchronized protected List<Card> getCenterCards() {
		return centerCards;
	}

	synchronized protected void setCenterCards(List<Card> centerCards) {
		this.centerCards = centerCards;
	}

	synchronized protected int getDealerIndex() {
		return dealerIndex;
	}

	synchronized protected void setDealerIndex(int dealerIndex) {
		this.dealerIndex = dealerIndex;
	}

	synchronized protected float getSmallBlind() {
		return smallBlind;
	}

	synchronized protected void setSmallBlind(float smallBlind) {
		this.smallBlind = smallBlind;
	}

	synchronized protected float getBigBlind() {
		return bigBlind;
	}

	synchronized protected void setBigBlind(float bigBlind) {
		this.bigBlind = bigBlind;
	}

	synchronized protected Turn getTurn() {
		return turn;
	}

	synchronized protected void setTurn(Turn turn) {
		this.turn = turn;
	}

	synchronized protected Lock getLock() {
		return lock;
	}

	synchronized protected void setLock(Lock lock) {
		this.lock = lock;
	}

	synchronized protected Group getGroup() {
		return group;
	}

	synchronized protected void setGroup(Group group) {
		this.group = group;
	}

	synchronized protected Duration getTurnDuration() {
		return turnDuration;
	}

	synchronized protected void setTurnDuration(Duration turnDuration) {
		this.turnDuration = turnDuration;
	}
	
	synchronized protected void addToPot(float amount) {
		this.pot += amount;
	}

	public float getRoundBet() {
		return roundBet;
	}

	public void setRoundBet(float roundBet) {
		this.roundBet = roundBet;
	}
}

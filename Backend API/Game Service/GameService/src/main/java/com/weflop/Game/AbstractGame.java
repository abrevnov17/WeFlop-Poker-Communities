package com.weflop.Game;

import java.io.IOException;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.TextMessage;

import com.weflop.Cards.Card;
import com.weflop.Database.GameRepository;
import com.weflop.Database.DomainObjects.CardPOJO;
import com.weflop.Database.DomainObjects.GameDocument;
import com.weflop.Database.DomainObjects.PlayerPOJO;
import com.weflop.Database.DomainObjects.SpectatorPOJO;
import com.weflop.Evaluation.HandRank;
import com.weflop.Evaluation.HandRankEvaluator;
import com.weflop.Networking.GameStatePOJO;
import com.weflop.Networking.LimitedPlayerPOJO;
import com.weflop.Networking.MessageSendingHandlers;
import com.weflop.Utils.ThreadExecution.TurnTimerManager;

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
	
	private float roundBet;
	
	private Group group; // our group of players

	private Turn turn;
		
	private HandRankEvaluator evaluator;
	
	private int round;
	
	private History history;
			
	private ScheduledExecutorService threadExecutor; // useful when creating timed events
	
	private int epoch; // value we increment on changes in state; keeps track of state versions
	
	private GameCustomMetadata metadata;
	
	@Autowired
	private GameRepository gameRepository;
		
	protected AbstractGame(GameCustomMetadata metadata, HandRankEvaluator evaluator) {
		this.metadata = metadata;
		this.id = UUID.randomUUID();
		this.setPot(0.0f);
		this.centerCards = new ArrayList<Card>();
		this.dealerIndex = 0;
		this.setStartTime(null); // do not start clock till start() called
		this.setGroup(new Group(metadata.getTableSize()));
		this.turn = null; // is not initialized until game starts
		this.setStarted(false);
		this.setLock(new ReentrantLock());
		this.setRoundBet(this.metadata.getBigBlind());
		this.evaluator = evaluator;
		this.setRound(0);
		this.threadExecutor = Executors
		        .newSingleThreadScheduledExecutor();
		this.epoch = 0;
	}
	
	public String getGameId() {
		return this.getId().toString();
	}
	
	/* These methods are publicly exposed and will be overriden by subclasses: */
	
	public abstract void performAction(Action action) throws Exception; // performs an action as a given participant
	
	/* Required methods (internally used) for all subclasses */
	protected abstract void deal(boolean dealNewHands); // deals cards to players
	protected abstract boolean isLastBettingRound(); // returns whether current round was last round of betting
	
	@Override
	public GameCustomMetadata getGameMetadata() {
		return this.metadata;
	}
	
	/**
	 * Handler for turn expirations. Called by a single thread after execution.
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
			this.performAction(new Action(ActionType.TURN_TIMEOUT, this.turn.getPlayer().getId()));
		} catch (Exception e) {
			// we do not need to do anything. this is only possible in an incredibly unlikely
			// race condition where the current players action is processed between the last if statement
			// and the current call to perform the action of a turn timeout. In this case, we can just
			// ignore the exception and consider this turn to be stale.
		}
	}
	
	/**
	 * Flushes game state and history to database.
	 */
	synchronized public void flushToDatabase() {
		GameDocument document = this.toDocument();
		this.getGameRepository().save(document);
	}
	
	/* Universally shared methods */
	
	/**
	 * Spawns a timer-thread that will send game packets when a turn has expired
	 */
	synchronized protected void beginTurnTimer() {
		    Runnable turnExpirationHandler = new TurnTimerManager(this, this.turn.getCount());

		    // resent packets after the duration of the turn has passed
		    threadExecutor.schedule(turnExpirationHandler, metadata.getTurnDuration().getSeconds(), TimeUnit.SECONDS);
	}
	
	
	/**
	 * Spawns a thread that periodically flushes the game state to the database.
	 */
	synchronized protected void spawnSaveGameThread() {
	    Runnable stateSaver = new Runnable() {
		      @Override
		      public void run() {
		    	  flushToDatabase();
		      }
		    };

		    // resent packets after the duration of the turn has passed
		    threadExecutor.scheduleAtFixedRate(stateSaver, 0, 60, TimeUnit.SECONDS);
	}
	
	/**
	 * Spawns a thread that periodically sends synchronization messages.
	 */
	synchronized protected void spawnSynchronizationPacketSendingThread() {
	    Runnable packetSender = new Runnable() {
		      @Override
		      public void run() {
		    	  flushToDatabase();
		      }
		    };

		    // resent packets after the duration of the turn has passed
		    threadExecutor.scheduleAtFixedRate(packetSender, 0, 2000, TimeUnit.MILLISECONDS);
	}
	
	synchronized protected void sendSynchronizationPackets() {
		long millisecondsRemaining = this.turn.getTimeRemaining(System.nanoTime(), metadata.getTurnDuration()).toMillis();
		try {
			MessageSendingHandlers.sendSynchronizationPackets(this.getGameId(), this.group, this.epoch, millisecondsRemaining);
		} catch (Exception e) {
			System.out.println(e.getStackTrace());
		}
	}
	
	/**
	 * Helper function to begin a new set of hands. Note that any locking must be handled
	 * outside of this function as this function is not inherently thread-safe.
	 * 
	 * Additionally, beginBettingRounds() expects an up-to-date dealer index and that all
	 * players have sufficient funds to play the current round.
	 * 
	 */
	synchronized protected void beginBettingRounds() {
		// first we deal cards
		this.deal(true); // Note: this also deals center cards and updates round number
		
		// Note: We do not need to check if the small/big blind can pay or not because that
		// should be done before calling this function
				
		// small blind pays
		this.getSmallBlindPlayer().bet(metadata.getSmallBlind());
		this.pot += metadata.getSmallBlind();
		
		// big blind pays
		this.getBigBlindPlayer().bet(metadata.getBigBlind());
		this.pot += metadata.getBigBlind();
		
		// resetting the current round bet amount (this is the most any player has bet during
		// the current round)
		this.setRoundBet(metadata.getBigBlind());
		
		this.beginNewRound();
	}
	
	/**
	 * Begins an individual round of betting.
	 * 
	 */
	synchronized protected void beginNewRound() {
		// flipping any new center cards
		this.deal(false); 

		// cycling to next turn
		cycleTurn(this.getBigBlindIndex());
	}
	
	/**
	 * Helper function called at the end of the last betting round. Updates dealer index and 
	 * checks to see that all players have sufficient funds to player the current round.
	 * 
	 */
	synchronized protected void endOfBettingRounds() {		
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
		
		this.pot = 0.0f; // resetting pot
		
		// removing any players with insufficient funds
		for (Player player : this.group.getPlayers()) {
			if (player.getBalance() < metadata.getBigBlind()) {
				this.bootPlayer(player, BootReason.INSUFFICIENT_FUNDS);
			}
		}
		
		// checking to see if we have enough players to continue
		if (this.group.getPlayers().size() < 2) {
			// TODO: this needs to be fleshed out
			this.setStarted(false); 
			this.threadExecutor.shutdown();
		}
		
		// propagating winners 
		this.propagateAction(new Action(ActionType.POT_WON, 
				playersWithMaxRank.stream().map(player -> player.getId()).collect(Collectors.toList())));
		this.incrementEpoch();
		
		// update dealer index
		this.dealerIndex = this.getNextPlayerIndex(this.dealerIndex);
		
		// resets all players to the waiting for next turn state
		this.group.setAllPlayersToState(PlayerState.WAITING_FOR_TURN);
		
		// resetting and starting next set of betting rounds
		this.beginBettingRounds();
	}
	
	synchronized protected void bootPlayer(Player player, BootReason reason) {
		this.group.movePlayerToSpectator(player);
		switch(reason) {
			case INSUFFICIENT_FUNDS:
				try {
					player.getSession().sendMessage(new TextMessage("Insufficient funds"));
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			case MISCONDUCT:
				try {
					player.getSession().sendMessage(new TextMessage("Misconduct"));
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			case FORBIDDEN_ACTIVITY:
				try {
					player.getSession().sendMessage(new TextMessage("Forbidden activity"));
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			default:
				try {
					player.getSession().sendMessage(new TextMessage("Player booted."));
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
		}
		this.propagateAction(new Action(ActionType.DISCONNECT, player.getId()));
	}
	
	synchronized protected void endBettingRound() {
		if (this.isLastBettingRound()) {
			this.endOfBettingRounds();
		} else {
			this.beginNewRound();
		}
	}
	
	/**
	 * Given an index of the player in the last turn (or the big blind index
	 * in the case that this is the start of a round), updates the turn to the
	 * next eligible player who is not all-in.
	 * 
	 * Also, this function checks to see if the round has ended and calls the
	 * appropriate handler for that case.
	 * 
	 * @param lastTurnIndex
	 */
	synchronized protected void cycleTurn(int lastTurnIndex) {
		// checking to see if the round is over
		if (isRoundOver()) {
			this.endBettingRound();
		}
		// setting current turn to be person
		int nextPlayerIndex = getNextPlayerIndex(lastTurnIndex);
		Player nextPlayer = this.group.getPlayers().get(nextPlayerIndex);
		
		while (nextPlayer.getState() == PlayerState.ALL_IN 
				|| nextPlayer.getState() == PlayerState.WAITING_FOR_ROUND) {
			// this player is either all-in or not playing this round, so we skip over them
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
	synchronized protected boolean isRoundOver() {
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
	
	/**
	 * Returns the document corresponding to the current game state.
	 * 
	 * @return Game state as GameDocument
	 */
	synchronized protected GameDocument toDocument() {
		List<CardPOJO> centerCards = this.centerCards.stream()
		        .map(card -> new CardPOJO(card.getSuit().getValue(), 
		        		card.getCardValue().getValue()))
		        .collect(Collectors.toList());
		
		List<PlayerPOJO> players = this.group.getPlayers().stream()
		        .map(player -> player.toPlayerPOJO())
		        .collect(Collectors.toList());
		
		List<SpectatorPOJO> spectators = this.group.getSpectators().stream()
		        .map(spectator -> spectator.toSpectatorPOJO())
		        .collect(Collectors.toList());
				
		return new GameDocument(id.toString(), metadata.getType().getValue(), 
				startTime.toEpochMilli(), metadata.getSmallBlind(), metadata.getBigBlind(), centerCards, 
				pot, dealerIndex, players, spectators, history.toPOJO());
	}
	
	/**
	 * Once an action has been verified by the server, we save it to our
	 * game history and propagate that information to user sessions.
	 * 
	 * @param action
	 */
	synchronized protected void propagateAction(Action action) {
		// add action to game history
		this.history.appendActionToSequence(action);
		this.incrementEpoch();

		// propagate action to members of group
		try {
			if (action.isUserAction()) {
				MessageSendingHandlers.propagateIncomingAction(this.getGameId(), this.getGroup(), 
					action, this.getEpoch());
			} else {
				if (action.getPlayerId() != null) {
					// message is sent to individual player
					MessageSendingHandlers.propagateOutgoingActionToPlayer(this.getGameId(), 
						this.getParticipantById(action.getPlayerId()),
						action, this.getEpoch());
				} else {
					// message is sent to all players
					MessageSendingHandlers.propagateOutgoingAction(this.getGameId(), 
							this.group, action, this.getEpoch());
				}
			}
		} catch (Exception e) {
			System.out.println(e.getStackTrace());
		}
	}
	
	/**
	 * Sends a player an updated game state (with limited view on other player states).
	 * Called on player join and on disconnects and any loss of synchronization.
	 * 
	 * @param player
	 */
	synchronized protected void sendUserGameState(Player player) {
		// sends player an updated game state
		try {
			MessageSendingHandlers.sendGameState(player, getGameStatePOJO(player));
		} catch (Exception e) {
			System.out.println(e.getStackTrace());
		}
	}
	
	/**
	 * Gets instance of GameStatePOJO used to update players on game state.
	 * @return Instance of GameStatePOJO
	 */
	synchronized protected GameStatePOJO getGameStatePOJO(Player player) {
		List<CardPOJO> centerCardsPOJO = this.centerCards.stream()
		        .map(card -> new CardPOJO(card.getSuit().getValue(), 
		        		card.getCardValue().getValue()))
		        .collect(Collectors.toList());
		
		List<LimitedPlayerPOJO> otherPlayers = this.group.getPlayers().stream()
                .filter(p -> !p.equals(player)) // filtering out current player
		        .map(p -> LimitedPlayerPOJO.fromPlayerPOJO(p.toPlayerPOJO()))
		        .collect(Collectors.toList());
		
		return new GameStatePOJO(this.getGameId(), centerCardsPOJO, pot, otherPlayers, 
				player.toPlayerPOJO(), turn.getPlayer().getId(), this.getEpoch());
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
	
	synchronized protected Player getParticipantById(String id) throws Exception {
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
	
	synchronized protected void addToCenterCards(Card card) {
		this.getCenterCards().add(card);
	}

	synchronized protected int getDealerIndex() {
		return dealerIndex;
	}

	synchronized protected void setDealerIndex(int dealerIndex) {
		this.dealerIndex = dealerIndex;
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
	
	synchronized protected void addToPot(float amount) {
		this.pot += amount;
	}

	synchronized protected float getRoundBet() {
		return roundBet;
	}

	synchronized protected void setRoundBet(float roundBet) {
		this.roundBet = roundBet;
	}

	synchronized protected int getRound() {
		return round;
	}

	synchronized protected void setRound(int round) {
		this.round = round;
	}
	
	synchronized protected void incrementRound() {
		this.round++;
	}
	
	synchronized protected void discardCenterCards() {
		this.centerCards.clear();
	}

	synchronized protected History getHistory() {
		return history;
	}

	synchronized protected void setHistory(History history) {
		this.history = history;
	}

	synchronized protected GameRepository getGameRepository() {
		return gameRepository;
	}

	synchronized protected void setGameRepository(GameRepository gameRepository) {
		this.gameRepository = gameRepository;
	}

	synchronized protected int getEpoch() {
		return epoch;
	}
	
	synchronized protected void incrementEpoch() {
		this.epoch++;
	}
}

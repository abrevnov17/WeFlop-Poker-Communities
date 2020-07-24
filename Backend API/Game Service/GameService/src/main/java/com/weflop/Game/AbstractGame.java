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
import com.weflop.Evaluation.HandRank;
import com.weflop.Evaluation.HandRankEvaluator;
import com.weflop.GameService.Database.GameRepository;
import com.weflop.GameService.Database.DomainObjects.CardPOJO;
import com.weflop.GameService.Database.DomainObjects.GameDocument;
import com.weflop.GameService.Database.DomainObjects.PlayerPOJO;
import com.weflop.GameService.Database.DomainObjects.SpectatorPOJO;
import com.weflop.GameService.Networking.GameStatePOJO;
import com.weflop.GameService.Networking.LimitedPlayerPOJO;
import com.weflop.GameService.Networking.MessageSendingHandlers;
import com.weflop.GameService.Networking.WebSocketHandler;
import com.weflop.Utils.ThreadExecution.TurnTimerManager;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 
 * Abstract class defining some shared properties and methods as well as some
 * required abstract methods for all instances of games.
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
		this.turn = null; // not updated until game begins
		this.setStarted(false);
		this.setLock(new ReentrantLock());
		this.setRoundBet(this.metadata.getBigBlind());
		this.evaluator = evaluator;
		this.setRound(0);
		this.threadExecutor = Executors.newSingleThreadScheduledExecutor();
		this.epoch = 0;
	}

	public String getGameId() {
		return this.getId().toString();
	}

	/* These methods are publicly exposed and will be overriden by subclasses: */

	public abstract void performAction(Action action) throws Exception; // performs an action as a given participant

	/* Required methods (internally used) for all subclasses */
	protected abstract void dealHands(); // deals cards to players
	protected abstract void dealCenterCards(); // deals center cards for current round
	protected abstract void dealRemainingCenterCards(); // deals any center cards that have not yet been dealt

	protected abstract boolean isLastBettingRound(); // returns whether current round was last round of betting

	@Override
	public GameCustomMetadata getGameMetadata() {
		return this.metadata;
	}

	/**
	 * Handler for turn expirations. Called by a single thread after execution.
	 */
	synchronized public void turnExpired(int turnCount) {
		System.out.println("Turn expired handler called...");
		// check to see if provided turn count matches current turn
		if (turnCount != this.turn.getCount()) {
			System.out.println("Stale turn expiration...");
			return; // this call is being applied to a stale turn
		}

		// otherwise, we know that a user has failed to perform an action in the alloted
		// time. in such cases, the user performs a TURN_TIMEOUT action, which (will
		// likely
		// be handled in a similar manner as to a fold)
		try {
			System.out.println("Performing turn timeout...");
			this.performAction(new Action.ActionBuilder(ActionType.TURN_TIMEOUT).withPlayerId(this.turn.getPlayer().getId()).build());
		} catch (Exception e) {
			// we do not need to do anything. this is only possible in an incredibly
			// unlikely
			// race condition where the current players action is processed between the last
			// if statement
			// and the current call to perform the action of a turn timeout. In this case,
			// we can just
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
				sendSynchronizationPackets();
			}
		};

		// resent packets after the duration of the turn has passed
		threadExecutor.scheduleAtFixedRate(packetSender, 0, 2000, TimeUnit.MILLISECONDS);
	}

	synchronized protected void sendSynchronizationPackets() {
		System.out.println("Sending sync packet for turn: " + turn.getCount() + " with start: " + turn.getStartTime());
		printGameState();
		
//		long millisecondsRemaining = this.turn.getTimeRemaining(System.nanoTime(), metadata.getTurnDuration())
//				.toMillis();
//		try {
//			MessageSendingHandlers.sendSynchronizationPackets(this.getGameId(), this.group, this.epoch,
//					millisecondsRemaining);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}

	/**
	 * Helper function to begin a new set of hands. Note that any locking must be
	 * handled outside of this function as this function is not inherently
	 * thread-safe.
	 * 
	 * Additionally, beginBettingRounds() expects an up-to-date dealer index and
	 * that all players have sufficient funds to play the current round.
	 * 
	 */
	synchronized protected void beginBettingRounds() {

		// Note: We do not need to check if the small/big blind can pay or not because
		// that
		// should be done before calling this function

		System.out.println("beggining new betting round");
		
		System.out.printf("Dealer index: %d, small blind index: %d, big blind index: %d\n", 
				this.dealerIndex, this.getSmallBlindIndex(), this.getBigBlindIndex());

		// small blind pays
		this.getSmallBlindPlayer().bet(metadata.getSmallBlind());
		this.pot += metadata.getSmallBlind();
		
		System.out.println("Paying small blind...");
		this.propagateActionToGroup(new Action.ActionBuilder(ActionType.SMALL_BLIND)
				.withPlayerId(getSmallBlindPlayer().getId())
				.withValue(metadata.getSmallBlind()).build());

		// big blind pays
		this.getBigBlindPlayer().bet(metadata.getBigBlind());
		this.pot += metadata.getBigBlind();
		
		System.out.println("Paying big blind...");
		this.propagateActionToGroup(new Action.ActionBuilder(ActionType.BIG_BLIND)
				.withPlayerId(getBigBlindPlayer().getId())
				.withValue(metadata.getBigBlind()).build());

		// resetting the current round bet amount (this is the most any player has bet
		// during the current round)
		this.setRoundBet(metadata.getBigBlind());

		// resets all players to the waiting for next turn state
		this.group.setAllPlayersToState(PlayerState.WAITING_FOR_TURN);
		
		beginNewRound(true);
	}

	/**
	 * Begins an individual round of betting.
	 * 
	 * @param dealPlayersCards Boolean indicating whether to deal player hands (only
	 * used after end of set of betting rounds).
	 */
	synchronized protected void beginNewRound(boolean dealPlayersCards) {
		System.out.println("beggining new round");
		
		if (dealPlayersCards) {
			dealHands(); // dealing hands to players
		}

		// flipping any new center cards
		dealCenterCards();
		
		// cycling to next turn
		if (dealPlayersCards) {
			cycleTurn(this.getBigBlindIndex()); // after paying blinds, player clockwise to big blind goes first
		} else {
			cycleTurn(this.dealerIndex); // during normal round without player cards dealt, start with player to right of dealer
		}
	}

	/**
	 * Helper function called at the end of the last betting round. Updates dealer
	 * index and checks to see that all players have sufficient funds to player the
	 * current round.
	 * 
	 */
	synchronized protected void endOfBettingRounds() {
		System.out.println("End of betting rounds...");
		// need to deal the remaining center cards (if any)
		this.dealRemainingCenterCards();
		
		// calculate winning hand(s)
		List<Player> playersWithMaxRank = new ArrayList<Player>();
		HandRank maxRank = null;
		for (Player player : this.group.getPlayers()) {
			if (player.getState() != PlayerState.FOLDED) {
				HandRank rank = this.evaluator.evaluate(this.centerCards, player.getCards());
				if (maxRank == null || rank.compareTo(maxRank) == 0) {
					// either first hand rank we have found or tied with best hand rank we have found
					maxRank = rank;
					playersWithMaxRank.add(player);
				} else if (rank.compareTo(maxRank) > 0) {
					// hand rank is best we have found so far
					playersWithMaxRank.clear();
					maxRank = rank;
					playersWithMaxRank.add(player);
				}
			}
		}
		
		this.printGameState();

		// distribute funds to winner(s)
		float perPlayerWinnings = this.pot / playersWithMaxRank.size(); // split pot between winners
		for (Player player : playersWithMaxRank) {
			player.increaseBalance(perPlayerWinnings);
		}
		
		this.printGameState();

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
			System.out.println("Ending game due to insufficient players...");
			this.setStarted(false);
			this.threadExecutor.shutdown();
			return;
		}

		System.out.println("Sending hand win/loss messages...");

		// propagating winners
		this.propagateActionToGroup(new Action.ActionBuilder(ActionType.POT_WON).withPlayerIds(
				playersWithMaxRank.stream().map(player -> player.getId()).collect(Collectors.toList())).build());

		// update dealer index
		this.dealerIndex = this.getNextPlayerIndex(this.dealerIndex);

		System.out.println("New dealer index: " + dealerIndex);
		
		// updating round
		this.setRound(0);

		// resetting and starting next set of betting rounds
		this.beginBettingRounds();
	}

	synchronized protected void bootPlayer(Player player, BootReason reason) {
		this.group.movePlayerToSpectator(player);
		switch (reason) {
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
		this.propagateActionToGroup(new Action.ActionBuilder(ActionType.DISCONNECT).withPlayerId(player.getId()).build());
	}

	synchronized protected void endBettingRound() {
		setRoundBet(0.0f); // resetting round bet
		group.resetPlayerRoundBets(); // setting all player round bets to 0
		group.preparePlayerStatesForNewRound(); // prepares player states for new round
		if (this.isLastBettingRound()) {
			this.endOfBettingRounds();
		} else {
			this.incrementRound();
			this.beginNewRound(false);
		}
	}

	/**
	 * Given an index of the player in the last turn (or the big blind index in the
	 * case that this is the start of a round), updates the turn to the next
	 * eligible player who is not all-in.
	 * 
	 * Also, this function checks to see if the round has ended and calls the
	 * appropriate handler for that case.
	 * 
	 * @param lastTurnIndex
	 */
	synchronized protected void cycleTurn(int lastTurnIndex) {
		System.out.printf("Cycling turn from %d...\n", lastTurnIndex);
		
		// checking to see if the round is over
		if (isRoundOver()) {
			System.out.println("Round over, ending betting round...");
			this.endBettingRound();
			return;
		}

		Player nextPlayer = getNextValidPlayer(lastTurnIndex);

		// updating turn state
		if (turn == null) {
			turn = new Turn(nextPlayer, System.nanoTime());
			nextPlayer.setState(PlayerState.CURRENT_TURN);
		} else {
			turn.nextTurn(nextPlayer);
			
		}

		System.out.printf("new turn player id: %s\n", nextPlayer.getId());

		// starting the turn timer
		this.beginTurnTimer();
	}

	/**
	 * A round is over when every player has either folded, bet the 'roundBet' (i.e.
	 * bet as much as the most any player has bet that round), or has gone all in. Or, 
	 * when considering a non-preflop round, if every player has checked.
	 * 
	 * @return A boolean indicating if the round is over.
	 */
	synchronized protected boolean isRoundOver() {
		// checking case applicable for non-preflop rounds where everyone has checked
		if (this.roundBet == 0.0f && group.allWaitingPlayersInCheckedState()) {
			return true;
		} else if (this.roundBet == 0.0f) {
			return false;
		}
		
		for (Player player : this.group.getPlayers()) {
			if (!(!player.canMoveInRound() 
					|| player.getCurrentRoundBet() == this.roundBet)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Checks that it is a given player's current turn. Throws an exception if it is
	 * not.
	 * 
	 * @param player
	 * @throws Exception
	 */
	synchronized protected void assertIsPlayerTurn(Player player) throws Exception {
		if (!this.getTurn().getPlayer().equals(player)) {
			throw new Exception("Player cannot perform that action " + "as it is no longer their turn.");
		}
	}

	/**
	 * Returns the document corresponding to the current game state.
	 * 
	 * @return Game state as GameDocument
	 */
	synchronized protected GameDocument toDocument() {
		List<CardPOJO> centerCards = this.centerCards.stream()
				.map(card -> new CardPOJO(card.getSuit().getValue(), card.getCardValue().getValue()))
				.collect(Collectors.toList());

		List<PlayerPOJO> players = this.group.getPlayers().stream().map(player -> player.toPlayerPOJO())
				.collect(Collectors.toList());

		List<SpectatorPOJO> spectators = this.group.getSpectators().stream()
				.map(spectator -> spectator.toSpectatorPOJO()).collect(Collectors.toList());

		return new GameDocument(id.toString(), metadata.getType().getValue(), startTime.toEpochMilli(),
				metadata.getSmallBlind(), metadata.getBigBlind(), centerCards, pot, dealerIndex, players, spectators,
				history.toPOJO());
	}
	
	/**
	 * Takes in an action and propagates it to individual participant.
	 * @param action
	 */
	synchronized protected void propagateActionToPlayer(Action action, Player participant) {
		List<Player> targets = new ArrayList<Player>();
		targets.add(participant);
		propagateAction(action, targets);
	}

	
	/**
	 * Takes in an action and propagates it to entire group (spectators + players).
	 * @param action
	 */
	synchronized protected void propagateActionToGroup(Action action) {
		propagateAction(action, group.getAllParticipants());
	}

	/**
	 * Once an action has been verified by the server, we save it to our game
	 * history and propagate that information to user sessions.
	 * 
	 * @param action
	 */
	synchronized protected void propagateAction(Action action, List<Player> targets) {
		// add action to game history
		if (started) {
			this.history.appendActionToSequence(action);
			this.incrementEpoch();
		}

		try {
			if (action.isUserAction()) {
				MessageSendingHandlers.propagateIncomingAction(this.getGameId(), this.getGroup(), action,
						this.getEpoch(), targets);
			} else {
				MessageSendingHandlers.propagateOutgoingAction(this.getGameId(), action,
							this.getEpoch(), targets);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sends a player an updated game state (with limited view on other player
	 * states). Called on player join and on disconnects and any loss of
	 * synchronization.
	 * 
	 * @param player
	 */
	synchronized protected void sendUserGameState(Player player) {
		// sends player an updated game state
		try {
			MessageSendingHandlers.sendGameState(player, getGameStatePOJO(player));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Gets instance of GameStatePOJO used to update players on game state.
	 * 
	 * @return Instance of GameStatePOJO
	 */
	synchronized protected GameStatePOJO getGameStatePOJO(Player player) {
		List<CardPOJO> centerCardsPOJO = this.centerCards.stream()
				.map(card -> new CardPOJO(card.getSuit().getValue(), card.getCardValue().getValue()))
				.collect(Collectors.toList());

		List<LimitedPlayerPOJO> otherPlayers = this.group.getPlayers().stream().filter(p -> !p.equals(player)) // filtering
				// out
				// current
				// player
				.map(p -> LimitedPlayerPOJO.fromPlayerPOJO(p.toPlayerPOJO())).collect(Collectors.toList());

		return new GameStatePOJO(this.getGameId(), centerCardsPOJO, pot, otherPlayers, player.toPlayerPOJO(),
				turn != null ? turn.getPlayer().getId() : null, this.getEpoch());
	}

	/**
	 * Prints information about the game useful for testing and debugging.
	 */
	synchronized protected void printGameState() {
		System.out.println("------------------------GAME STATE------------------------");
		System.out.printf("Pot: %.2f\n", this.pot);
		System.out.printf("Round: %d, Round bet: %.2f\n", this.round, this.roundBet);
		System.out.printf("Center cards: %s\n", WebSocketHandler.GSON.toJson(this.centerCards));

		for (Player player : group.getPlayers()) {
			System.out.printf("Player id: %s, state: %s, round_bet: %.2f, current_bet: %.2f, balance: %.2f, slot: %d, cards: %s\n", 
					player.getId(), WebSocketHandler.GSON.toJson(player.getState()), player.getCurrentRoundBet(), player.getCurrentBet(), 
					player.getBalance(), player.getSlot(), WebSocketHandler.GSON.toJson(player.getCards()));
		}
		
		System.out.printf("Turn player id: %s\n", turn != null ? turn.getPlayer().getId() : "no turn exists yet");
		System.out.printf("Dealer index: %d\n", this.dealerIndex);
		System.out.println("------------------------END GAME STATE------------------------");
	}
	
	/* Getters and setters for universal game properties */

	/**
	 * Gets index of next valid player (i.e. not folded, all-in, etc...)
	 * given index of player with last turn.
	 * 
	 * @param lastTurnIndex
	 * @return Next player that is eligible to hold a turn.
	 */
	synchronized protected Player getNextValidPlayer(int lastTurnIndex) {
		int nextPlayerIndex = getNextPlayerIndex(lastTurnIndex);
		Player nextPlayer = this.group.getPlayers().get(nextPlayerIndex);

		while (!nextPlayer.canMoveInRound()) {
			// this player is either all-in, folded, or not playing this round
			nextPlayerIndex = getNextPlayerIndex(lastTurnIndex);
			nextPlayer = this.group.getPlayers().get(nextPlayerIndex);
		}
		
		return nextPlayer;
	}
	
	
	/**
	 * Gets the index of the next player (clockwise) in our group of players given
	 * the index of the previous player in the group.
	 * 
	 * @param index
	 *            Index of player whose turn is before the current player
	 * @return The index of the desired player
	 */
	synchronized protected int getNextPlayerIndex(int index) {
		return (index + 1) % this.group.getPlayers().size();
	}

	synchronized protected Player getNextPlayer(int index) {
		return this.group.getPlayers().get(getNextPlayerIndex(index));
	}

	synchronized protected int getSmallBlindIndex() {
		return getNextPlayerIndex(dealerIndex);
	}

	synchronized protected Player getSmallBlindPlayer() {
		return this.group.getPlayers().get(getSmallBlindIndex());
	}

	synchronized protected int getBigBlindIndex() {
		return getNextPlayerIndex(getSmallBlindIndex());
	}

	synchronized protected Player getBigBlindPlayer() {
		return this.group.getPlayers().get(getBigBlindIndex());
	}

	synchronized protected Player getParticipantById(String id) throws Exception {
		for (Player participant : group.getAllParticipants()) {
			if (participant.getId().equals(id)) {
				return participant;
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

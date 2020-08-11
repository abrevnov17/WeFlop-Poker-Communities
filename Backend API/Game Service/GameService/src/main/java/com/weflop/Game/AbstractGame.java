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

import com.weflop.Cards.Board;
import com.weflop.Cards.Card;
import com.weflop.Evaluation.HandRankEvaluator;
import com.weflop.GameService.Database.GameRepository;
import com.weflop.GameService.Database.DomainObjects.GameDocument;
import com.weflop.GameService.Database.DomainObjects.PlayerPOJO;
import com.weflop.GameService.Database.DomainObjects.SpectatorPOJO;
import com.weflop.GameService.Networking.GameStatePOJO;
import com.weflop.GameService.Networking.LimitedPlayerPOJO;
import com.weflop.GameService.Networking.MessageSendingHandlers;
import com.weflop.GameService.Networking.WebSocketHandler;
import com.weflop.GameService.REST.GameMetadata;
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

	private BetController betController;

	private Board board;
	private int dealerIndex;

	private Group group; // our group of players

	private Turn turn;

	private HandRankEvaluator evaluator;

	private int round;

	private History history;

	private ScheduledExecutorService threadExecutor; // useful when creating timed events

	private int epoch; // value we increment on changes in state; keeps track of state versions

	private GameCustomMetadata metadata;
	
	private boolean active;
	
	// initially false. if we flush to database and there are no players/spectators, it becomes true.
	// if we flush to database and there are no players/spectators and this is true, we delete the current
	// game from this replica
	private boolean inactiveForPeriod; 

	@Autowired
	private GameRepository gameRepository;
	
	protected AbstractGame(GameCustomMetadata metadata, HandRankEvaluator evaluator) {
		this.metadata = metadata;
		this.id = UUID.randomUUID();
		this.betController = new BetController(metadata.getSmallBlind(), metadata.getBigBlind(), 
				metadata.getMinBuyIn(), metadata.getMaxBuyIn());
		this.setBoard(new Board());
		this.dealerIndex = 0;
		this.setStartTime(null); // do not start clock till start() called
		this.setGroup(new Group(metadata.getTableSize()));
		this.turn = null; // not updated until game begins
		this.setStarted(false);
		this.setLock(new ReentrantLock());
		this.evaluator = evaluator;
		this.setRound(0);
		this.threadExecutor = Executors.newSingleThreadScheduledExecutor();
		this.epoch = 0;
		this.active = true;
		this.inactiveForPeriod = false;
	}
	
	/**
	 * Constructor used to load an existing game from a database.
	 * @param document
	 */
	protected AbstractGame(GameDocument document) {
		this.id = UUID.fromString(document.getId());
		this.metadata = document.getMetadata();
		this.betController = new BetController(metadata.getSmallBlind(), metadata.getBigBlind(), 
				metadata.getMinBuyIn(), metadata.getMaxBuyIn());
		this.board = new Board(document.getCenterCards());
	}

	@Override
	public String getGameId() {
		return this.getId().toString();
	}

	/* These methods are publicly exposed and will be overriden by subclasses: */

	@Override
	public abstract void performAction(Action action) throws Exception; // performs an action as a given participant

	/* Required methods (internally used) for all subclasses */
	protected abstract void dealHands(); // deals cards to players
	protected abstract void dealCenterCards(); // deals center cards for current round
	protected abstract void dealRemainingCenterCards(); // deals any center cards that have not yet been dealt

	protected abstract boolean isLastBettingRound(); // returns whether current round was last round of betting

	@Override
	public GameMetadata getGameMetadata() {
		return new GameMetadata(started ? startTime.toEpochMilli() : -1, betController.getTotalPot(), 
				metadata, betController.getLedger().toPOJO());
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
			// unlikely race condition where the current players action is processed between the last
			// if statement and the current call to perform the action of a turn timeout. In this case,
			// we can just ignore the exception and consider this turn to be stale.
		}
	}

	/**
	 * Flushes game state and history to database.
	 */
	synchronized public void flushToDatabase() {
		if (group.getPlayers().size() == 0 && group.getSpectators().size() == 0) {
			if (this.inactiveForPeriod) {
				// deleting game from replica and canceling current operations		
				removeGameFromReplica();
			} else {
				this.inactiveForPeriod = true;
			}
		}
		GameDocument document = this.toDocument();
		this.getGameRepository().save(document);
	}
	
	/**
	 * Removes game from replica and cancels any related threads (best effort).
	 */
	synchronized public void removeGameFromReplica() {
		threadExecutor.shutdownNow();
		GameFactory.ID_TO_GAME.remove(id.toString());
	}

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
	 * that all players have sufficient funds to play the current round. Player
	 * states should be up-to-date as well
	 * 
	 */
	synchronized protected void beginBettingRounds() {
		// Note: We do not need to check if the small/big blind can pay or not because
		// that should be done before calling this function

		System.out.println("beggining new betting round");

		System.out.printf("Dealer index: %d, small blind index: %d, big blind index: %d\n", 
				this.dealerIndex, group.getSmallBlindIndex(), group.getBigBlindIndex());

		System.out.println("Paying blinds...");
		List<Action> blindPaymentActions = betController.payBlinds(group.getSmallBlindPlayer(), group.getBigBlindPlayer());

		System.out.println("Propogating blind payments...");

		for (Action action : blindPaymentActions) {
			this.propagateActionToGroup(action);
		}

		// resetting the current round bet amount (this is the most any player has bet
		// during the current round)
		betController.setRoundBet(metadata.getBigBlind());
		
		betController.postBigBlinds(group.getPlayersWhoHavePostedBigBlind());
		
		// beginning hand
		beginNewHand(group.getBigBlindIndex());
	}

	/**
	 * Begins an individual round of betting.
	 */
	synchronized protected void beginNewRound() {
		System.out.println("beggining new round");

		// flipping any new center cards
		dealCenterCards();

		// cycling to next turn
		cycleTurn(this.dealerIndex); // during normal round without player cards dealt, start with player to right of dealer
	}

	/**
	 * Begins a new hand.
	 * @param bigBlindIndex Index of big blind (used to determine where to cycle turn to).
	 */
	synchronized protected void beginNewHand(int bigBlindIndex) {
		System.out.println("beggining new hand");

		dealHands(); // dealing hands to players

		// flipping any new center cards
		dealCenterCards();

		// cycling to next turn
		cycleTurn(bigBlindIndex); // after paying blinds, player clockwise to big blind goes first
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

		// calculate side pots
		List<Player> activePlayers = group.getActivePlayersInBettingRound();
		List<Pot> pots = betController.endOfBettingRoundGeneratePots(activePlayers);

		// calculate winners and distribute side pots
		List<Action> actionsToPropagate = betController.distributePots(pots);

		// propagate information about pot winners
		for (Action action : actionsToPropagate) {
			this.propagateActionToGroup(action);
		}

		this.printGameState();

		this.betController.resetForNewHand();; // resetting betting information

		// removing any players with insufficient funds
		for (Player player : this.group.getPlayers()) {
			if (player.getBalance() < metadata.getBigBlind()) {
				this.bootPlayer(player, BootReason.INSUFFICIENT_FUNDS);
			}
		}

		// players transition to next state
		group.transitionPlayerStates();

		// checking to see if we have enough players to continue
		if (this.group.getActivePlayersInHand().size() < 2) {
			// TODO: this needs to be fleshed out
			System.out.println("Ending game due to insufficient players...");
			this.setStarted(false);
			return;
		}

		// update dealer index
		this.dealerIndex = this.getNextActivePlayerIndex(this.dealerIndex);

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
		group.resetPlayerRoundBets(); // setting all player round bets to 0
		group.preparePlayerStatesForNewRound(); // prepares player states for new round

		if (this.isLastBettingRound()) {
			this.endOfBettingRounds();
		} else {
			calculatePotsAndPropagateEndOfBettingRound();
			this.incrementRound();
			this.beginNewRound();
		}
	}

	/**
	 * Propagates messages to group updating current pot information and conveys
	 * that new betting round has begun.
	 */
	synchronized protected void calculatePotsAndPropagateEndOfBettingRound() {
		List<Float> pots = betController.endOfBettingRoundGeneratePots(group.getActivePlayersInBettingRound())
				.stream().map(pot -> pot.getSize()).collect(Collectors.toList());

		this.propagateActionToGroup(new Action.ActionBuilder(ActionType.BETTING_ROUND_OVER).withPots(pots).build());
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
		if (betController.getRoundBet() == 0.0f && group.allWaitingPlayersInCheckedState()) {
			return true;
		} else if (betController.getRoundBet()  == 0.0f) {
			return false;
		}

		for (Player player : this.group.getPlayers()) {
			if (!(!player.canMoveInRound() 
					|| player.getCurrentRoundBet() == betController.getRoundBet() )) {
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
		List<PlayerPOJO> players = this.group.getPlayers().stream().map(player -> player.toPlayerPOJO())
				.collect(Collectors.toList());

		List<SpectatorPOJO> spectators = this.group.getSpectators().stream()
				.map(spectator -> spectator.toSpectatorPOJO()).collect(Collectors.toList());

		return new GameDocument(id.toString(), metadata.getType(), started ? startTime.toEpochMilli() : -1, 
				board.toPOJO(), betController.getTotalPot(), dealerIndex, players, spectators, history.toPOJO(), 
				betController.getLedger().toPOJO(), this.metadata, this.active);
	}
	
	/**
	 * Attemps to archive the current game. Returns true if archive is successful, returns false otherwise.
	 * @return
	 */
	@Override
	public boolean archive(String userId) {
		// assert that we have no active players and that given user owns the current game
		if (group.getPlayers().size() != 0 || !metadata.getCreatedBy().equals(userId)) {
			return false;
		}
		
		// updating database entry
		this.active = false;
		this.flushToDatabase();
		
		// deleting game from replica and shutting down thread executor
		removeGameFromReplica();
		
		return true;
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
		List<LimitedPlayerPOJO> otherPlayers = this.group.getPlayers().stream().filter(p -> !p.equals(player)) // filtering
				// out
				// current
				// player
				.map(p -> LimitedPlayerPOJO.fromPlayerPOJO(p.toPlayerPOJO())).collect(Collectors.toList());

		return new GameStatePOJO(this.getGameId(), board.toPOJO(), betController.getTotalPot(), otherPlayers, player.toPlayerPOJO(),
				turn != null ? turn.getPlayer().getId() : null, this.getEpoch());
	}

	/**
	 * Prints information about the game useful for testing and debugging.
	 */
	synchronized protected void printGameState() {
		System.out.println("------------------------GAME STATE------------------------");
		System.out.printf("Pot: %.2f\n", betController.getTotalPot());
		System.out.printf("Round: %d, Round bet: %.2f\n", this.round, betController.getRoundBet() );
		System.out.printf("Center cards: %s\n", WebSocketHandler.GSON.toJson(this.board));

		for (Player player : group.getPlayers()) {
			System.out.printf("Player id: %s, state: %s, round_bet: %.2f, current_bet: %.2f, balance: %.2f, slot: %d, cards: %s\n", 
					player.getId(), WebSocketHandler.GSON.toJson(player.getState()), player.getCurrentRoundBet(), player.getCurrentBet(), 
					player.getBalance(), player.getSlot(), WebSocketHandler.GSON.toJson(player.getHand()));
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
		int nextPlayerIndex = getNextActivePlayerIndex(lastTurnIndex);
		Player nextPlayer = this.group.getActivePlayersInHand().get(nextPlayerIndex);

		while (!nextPlayer.canMoveInRound()) {
			// this player is either all-in, folded, or not playing this round
			nextPlayerIndex = getNextActivePlayerIndex(lastTurnIndex);
			nextPlayer = this.group.getActivePlayersInHand().get(nextPlayerIndex);
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
	synchronized protected int getNextActivePlayerIndex(int index) {
		return (index + 1) % this.group.getActivePlayersInHand().size();
	}

	synchronized protected Player getNextActivePlayer(int index) {
		return this.group.getActivePlayersInHand().get(getNextActivePlayerIndex(index));
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

	synchronized protected void addToCenterCards(Card card) {
		this.board.addCard(card);
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

	synchronized protected int getRound() {
		return round;
	}

	synchronized protected void setRound(int round) {
		this.round = round;
	}

	synchronized protected void incrementRound() {
		this.round++;
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

	synchronized protected Board getBoard() {
		return board;
	}

	synchronized protected void setBoard(Board board) {
		this.board = board;
	}

	protected synchronized HandRankEvaluator getEvaluator() {
		return evaluator;
	}

	protected synchronized void setEvaluator(HandRankEvaluator evaluator) {
		this.evaluator = evaluator;
	}

	protected synchronized BetController getBetController() {
		return betController;
	}

	protected synchronized void setBetController(BetController betController) {
		this.betController = betController;
	}

	protected GameCustomMetadata getMetadata() {
		return metadata;
	}

	protected void setMetadata(GameCustomMetadata metadata) {
		this.metadata = metadata;
	}

	protected ScheduledExecutorService getThreadExecutor() {
		return threadExecutor;
	}

	protected void setThreadExecutor(ScheduledExecutorService threadExecutor) {
		this.threadExecutor = threadExecutor;
	}

	protected boolean isActive() {
		return active;
	}

	protected void setActive(boolean active) {
		this.active = active;
	}

	protected boolean isInactiveForPeriod() {
		return inactiveForPeriod;
	}

	protected void setInactiveForPeriod(boolean inactiveForPeriod) {
		this.inactiveForPeriod = inactiveForPeriod;
	}

	protected void setEpoch(int epoch) {
		this.epoch = epoch;
	}
}

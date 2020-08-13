package com.weflop.Game.BasicPokerGame;

import java.time.Instant;

import java.util.ArrayList;

import org.springframework.util.Assert;

import com.weflop.Cards.Deck;
import com.weflop.Cards.StandardDeck;
import com.weflop.Evaluation.HandRankEvaluator;
import com.weflop.Game.AbstractGame;
import com.weflop.Game.Action;
import com.weflop.Game.ActionType;
import com.weflop.Game.BootReason;
import com.weflop.Game.GameCustomMetadata;
import com.weflop.Game.History;
import com.weflop.Game.InitialState;
import com.weflop.Game.Player;
import com.weflop.Game.PlayerState;
import com.weflop.GameService.Database.DomainObjects.GameDocument;

/**
 * This class represents an actual Poker game following the
 * 
 * @author abrevnov
 *
 */
public class BasicPokerGame extends AbstractGame {

	private VariantRepresentation variant;
	private Deck deck;

	/* Constructors */

	public BasicPokerGame(GameCustomMetadata metadata, HandRankEvaluator evaluator) {
		super(metadata, evaluator);
		this.variant = PokerVariants.getStandardHoldem(); // default is hold'em
		this.deck = new StandardDeck(); // default is standard 52 card deck
	}

	public BasicPokerGame(GameCustomMetadata metadata, VariantRepresentation variant, Deck deck,
			HandRankEvaluator evaluator) {
		super(metadata, evaluator);
		this.variant = variant;
		this.deck = deck;
	}
	
	/**
	 * Loads an existing game state as described by the game document.
	 * @param document
	 */
	public BasicPokerGame(GameDocument document, HandRankEvaluator evaluator) {
		super(document, evaluator);
	}

	/* Overrided methods from abstract superclass */

	@Override
	public void performAction(Action action) throws Exception {
		// locking game object
		this.getLock().lock();

		try {
			switch (action.getType()) {
			case START: {
				Assert.isTrue(this.getGroup().getPlayers().size() >= 2, "A game requires at least two players");
				Assert.isTrue(!this.isStarted(), "Game has already begun");

				Player participant = this.getParticipantById(action.getPlayerId());
				Assert.isTrue(!participant.isSpectating(), "Only seated players can start a game.");

				System.out.printf("Player %s starting game\n", action.getPlayerId());

				startGame();
			}
			break;
			case FOLD: {
				Assert.isTrue(this.isStarted(), "Game has not begun");

				Player participant = this.getParticipantById(action.getPlayerId());

				assertIsPlayerTurn(participant);

				// update player state to folded
				participant.setState(PlayerState.FOLDED);

				// move on to next turn
				this.cycleTurn(this.getGroup().getIndexOfPlayerInList(participant));

				// propagate action to members of group
				this.propagateActionToGroup(action);

				System.out.printf("Player %s folded\n", action.getPlayerId());
				participant.setDisplayingInactivity(false);
			}
			break;
			case RAISE: {
				Assert.isTrue(this.isStarted(), "Game has not begun");

				Player participant = this.getParticipantById(action.getPlayerId());

				assertIsPlayerTurn(participant);

				// get bet and check that it is valid
				float bet = action.getValue();

				// update player balances and pot
				getBetController().raise(participant, bet); // verification performed in 'bet' method

				// update player state to waiting for turn
				participant.setState(PlayerState.WAITING_FOR_TURN);

				// move on to next turn
				this.cycleTurn(this.getGroup().getIndexOfPlayerInList(participant));

				// propagate action to members of group
				this.propagateActionToGroup(action);

				System.out.printf("Player %s raised by: %d\n", action.getPlayerId(), bet);
				participant.setDisplayingInactivity(false);
			}
			break;
			case CALL: {
				Assert.isTrue(this.isStarted(), "Game has not begun");

				Player participant = this.getParticipantById(action.getPlayerId());

				assertIsPlayerTurn(participant);

				// update player balances and pot
				float bet = getBetController().getRoundBet() - participant.getCurrentRoundBet();

				// update player balances and pot
				getBetController().bet(participant, bet); // verification performed in 'bet' method
				
				// update player state to waiting for turn
				participant.setState(PlayerState.WAITING_FOR_TURN);

				// propagate action to members of group
				this.propagateActionToGroup(action);

				// move on to next turn
				this.cycleTurn(this.getGroup().getIndexOfPlayerInList(participant));

				System.out.printf("Player %s called\n", action.getPlayerId());
				participant.setDisplayingInactivity(false);
			}
			break;
			case CHECK: {
				Assert.isTrue(this.isStarted(), "Game has not begun");

				Player participant = this.getParticipantById(action.getPlayerId());

				Assert.isTrue(getBetController().getRoundBet() == participant.getCurrentRoundBet(), "Current player bet is insufficient to check.");

				assertIsPlayerTurn(participant);
				// update player state to waiting for turn
				participant.setState(PlayerState.CHECKED);

				// propagate action to members of group
				this.propagateActionToGroup(action);

				// move on to next turn
				this.cycleTurn(this.getGroup().getIndexOfPlayerInList(participant));

				System.out.printf("Player %s checked\n", action.getPlayerId());
				participant.setDisplayingInactivity(false);
			}
			break;
			case ALL_IN: {
				Assert.isTrue(this.isStarted(), "Game has not begun");
				Player participant = this.getParticipantById(action.getPlayerId());

				assertIsPlayerTurn(participant);

				float bet = getBetController().goAllIn(participant);
				
				action.setValue(bet); // appending bet value to action

				// propagate action to members of group
				this.propagateActionToGroup(action);

				// move on to next turn
				this.cycleTurn(this.getGroup().getIndexOfPlayerInList(participant));

				System.out.printf("Player %s went all-in\n", action.getPlayerId());
				participant.setDisplayingInactivity(false);
			}
			break;
			case TURN_TIMEOUT: {
				System.out.println("inside turn timeout");
				Player participant = this.getParticipantById(action.getPlayerId());

				assertIsPlayerTurn(participant);
				// we handle timeouts the same way as folding

				// update player state to folded
				participant.setState(PlayerState.FOLDED);
				participant.setNextHandState(PlayerState.WAITING_FOR_BIG_BLIND);

				// propagate action to members of group
				this.propagateActionToGroup(action);

				// move on to next turn
				this.cycleTurn(this.getGroup().getIndexOfPlayerInList(participant));

				System.out.printf("Player %s timed out\n", action.getPlayerId());
				
				if (participant.isDisplayingInactivity()) {
					// they have already missed a turn prior to this, so they get booted from the table
					this.bootPlayer(participant, BootReason.INACTIVITY);
				}
				participant.setDisplayingInactivity(true);

			}
			break;
			case JOIN: {
				printGameState();
				
				Player participant = getGroup().getParticipantById(action.getPlayerId());
				if (participant != null) {
					participant.setSession(action.getSession());
					System.out.printf("Player %s re-joining game\n", action.getPlayerId());
				} else {
					// add player as spectator
					this.getGroup().createSpectator(action.getPlayerId(), action.getSession());
	
					System.out.printf("Player %s joining game\n", action.getPlayerId());
	
					participant = this.getParticipantById(action.getPlayerId());
				}
				
				System.out.println(participant);
				
				// need to send the player the current game state
				this.sendUserGameState(participant);
				participant.setDisplayingInactivity(false);
			}
			break;
			case SIT: {
				Player participant = this.getParticipantById(action.getPlayerId());

				this.getGroup().moveSpectatorToActivePlayer(participant, action.getSlot());
				
				getBetController().buyIn(participant, action.getValue());
				
				System.out.printf("Player %s sitting\n", action.getPlayerId());
				
				// propagate action to members of group
				this.propagateActionToGroup(action);
				
				if (!this.isStarted() && this.getGroup().getPlayers().size() >= 2) {
					// all players who joined before start should be active in first hand
					getGroup().setAllPlayersCurrentAndFutureStates(PlayerState.WAITING_FOR_TURN, PlayerState.WAITING_FOR_TURN);
					
					this.startGame();
				}
				participant.setDisplayingInactivity(false);
			}
			break;
			case STAND: {
				Player participant = this.getParticipantById(action.getPlayerId());

				// transition player from player to spectator
				this.getGroup().movePlayerToSpectator(participant);

				// propagate action to members of group
				this.propagateActionToGroup(action);

				System.out.printf("Player %s stood\n", action.getPlayerId());
				participant.setDisplayingInactivity(false);
			}
			break;
			case DISCONNECT: {
				Player participant = this.getParticipantById(action.getPlayerId());

				this.getGroup().deleteParticipant(participant);

				// propagate action to members of group if not spectator
				if (!participant.isSpectating()) {
					this.propagateActionToGroup(action);
				}
				System.out.printf("Player %s disconnected\n", action.getPlayerId());
				participant.setDisplayingInactivity(false);
			}
			break;
			case SIT_OUT_HAND: {
				Player participant = this.getParticipantById(action.getPlayerId());
				
				Assert.isTrue(participant.canSitOut(), "Must be an active player to sit out.");
				
				// sitting player out for next round
				participant.setNextHandState(PlayerState.WAITING_FOR_HAND);
				
				this.propagateActionToGroup(action);
				participant.setDisplayingInactivity(false);
			}
			break;
			case SIT_OUT_BB: {
				Player participant = this.getParticipantById(action.getPlayerId());
				
				Assert.isTrue(participant.canSitOut(), "Must be an active player to sit out.");
				
				// sitting player out for next round
				participant.setNextHandState(PlayerState.SITTING_OUT_BB);
				
				this.propagateActionToGroup(action);
				participant.setDisplayingInactivity(false);
			}
			break;
			case POST_BIG_BLIND: {
				Player participant = this.getParticipantById(action.getPlayerId());
				
				Assert.isTrue(participant.canPostBigBlind(getMetadata().getBigBlind()), "Player cannot post big blind.");
				
				participant.updateCurrentAndFutureState(PlayerState.POSTING_BIG_BLIND, PlayerState.WAITING_FOR_HAND);
				
				this.propagateActionToGroup(action);
				participant.setDisplayingInactivity(false);
			}
			break;
			case TOP_OFF: {
				Player participant = this.getParticipantById(action.getPlayerId());
				
				getBetController().buyIn(participant, action.getValue());
				
				if (participant.getState() == PlayerState.BUSTED) {
					participant.updateCurrentAndFutureState(PlayerState.WAITING_FOR_BIG_BLIND, PlayerState.WAITING_FOR_BIG_BLIND);
				}
				
				System.out.printf("Player %s topping off...\n", action.getPlayerId());

				// do not propagate...propagate when we actually update balance
				participant.setDisplayingInactivity(false);
			}
			break;
			case CHANGE_SEAT: {
				Player participant = this.getParticipantById(action.getPlayerId());
				
				Assert.isTrue(!participant.canChangeSeat(), "Player is not able to change seats.");
								
				participant.sit(action.getSlot());
				
				// propagate action to members of group
				this.propagateActionToGroup(action);
				participant.setDisplayingInactivity(false);
			}
			break;
			case SHOW_CARDS: {
				Player participant = this.getParticipantById(action.getPlayerId());
				
				boolean permission = getGroup().getPlayersWhoCanMuck().remove(participant);
				
				if (!permission) {
					break;
				}
				
				this.incrementMuckDecisionTime();
				
				action.setCards(participant.getHand().getCards());
				this.propagateActionToGroup(action);
				participant.setDisplayingInactivity(false);
			} break;
			case MUCK_CARDS: {
				Player participant = this.getParticipantById(action.getPlayerId());
				
				boolean permission = getGroup().getPlayersWhoCanMuck().remove(participant);
				
				if (!permission) {
					break;
				}
				
				this.incrementMuckDecisionTime();
				
				this.propagateActionToGroup(action);
				participant.setDisplayingInactivity(false);
			} break;
			case AUTO_CALL: {
				Player participant = this.getParticipantById(action.getPlayerId());
				Assert.isTrue(participant.getState() == PlayerState.WAITING_FOR_TURN, "Player must be waiting for turn to pre-move.");
				
				participant.setState(PlayerState.AUTO_CALL);
				participant.setDisplayingInactivity(false);
			} break;
			case AUTO_CHECK_OR_FOLD: {
				Player participant = this.getParticipantById(action.getPlayerId());
				Assert.isTrue(participant.getState() == PlayerState.WAITING_FOR_TURN, "Player must be waiting for turn to pre-move.");

				participant.setState(PlayerState.AUTO_CHECK_OR_FOLD);
				participant.setDisplayingInactivity(false);
			} break;
			default:
				throw new Exception("Unsopported action for this game mode");
			}
		} finally {
			// releasing game lock
			this.getLock().unlock();
		}

	}
	
	private void startGame() {
		// initializing game history
		InitialState state = new InitialState(this.getGroup().getPlayers());
		this.setHistory(new History(state, new ArrayList<Action>()));

		// start game clock
		this.setStarted(true);
		this.setStartTime(Instant.now());

		// propagate action that game has started to all members of group
		this.propagateActionToGroup(new Action.ActionBuilder(ActionType.START).build());

		// start betting rounds
		this.beginBettingRounds();

		// spawning a thread that periodically saves the game
		spawnSaveGameThread();
		
		// spawning a thread that updates player states
		spawnSynchronizationPacketSendingThread();
	}

	/**
	 * Deals new hands to each player
	 */
	@Override
	protected void dealHands() {
		// shuffling deck
		deck.shuffle();
		System.out.println("dealing new hands");
		// deal cards to players
		int numDealt = this.variant.getNumDealt();
		for (Player player : this.getGroup().getPlayers()) {
			player.discardHand();; // discarding any old hands
			for (int i = 0; i < numDealt; i++) {
				player.addCard(deck.dealCard());
			}

			if (numDealt > 0) {
				// sending message to player with new cards
				this.propagateActionToPlayer(new Action.ActionBuilder(ActionType.PLAYER_DEAL).withCards(player.getHand().getCards()).build(), player);
			}
		}

		getBoard().discard();
	}

	/**
	 * Given a list of integers representing round numbers, deals center cards for each
	 * round and propagates that update to players and spectators.
	 * 
	 * @param rounds
	 */
	@Override
	protected void dealCenterCards() {
		int newCenterCards = this.variant.getCardsDealtBeforeRound(this.getRound());
		System.out.printf("dealing %d center cards for round %d\n", newCenterCards, this.getRound());
		dealCenterCards(newCenterCards);
	}
	
	/**
	 * Deals all center cards that have yet to be dealt.
	 */
	@Override
	protected void dealRemainingCenterCards() {
		int newCenterCards =  this.variant.getTotalCardsDealt() - this.getBoard().getCards().size();
		System.out.printf("dealing %d remaining center cards\n", newCenterCards);
		dealCenterCards(newCenterCards);
	}
	
	/**
	 * Helper method that deals a set number of center cards and propagates updates to participants.
	 * @param numCards
	 */
	private void dealCenterCards(int numCards) {
		// deal center cards
		for (int i = 0; i < numCards; i++) {
			this.addToCenterCards(deck.dealCard());
		}

		// messaging players regarding new center cards
		if (numCards > 0) {
			this.propagateActionToGroup(new Action.ActionBuilder(ActionType.CENTER_DEAL).withCards(getBoard().getCards()).build());
			this.incrementEpoch();
		}
	}

	/**
	 * Determines if it is the last round.
	 * 
	 */
	@Override
	protected boolean isLastBettingRound() {
		if (this.getRound() == this.variant.getBettingRounds()) {
			return true;
		}

		// if all players have folded or are all-in, the round is over
		int count = 0;
		for (Player player : getGroup().getPlayers()) {
			if (!player.isActive()) {
				count++;
			}
		}

		if (count > 1) {
			return false;
		}

		return true;
	}
}

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
import com.weflop.Game.GameCustomMetadata;
import com.weflop.Game.History;
import com.weflop.Game.InitialState;
import com.weflop.Game.Player;
import com.weflop.Game.PlayerState;

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

				System.out.printf("Player %s starting\n", action.getPlayerId());

				// initializing game history
				InitialState state = new InitialState(this.getGroup().getPlayers());
				this.setHistory(new History(state, new ArrayList<Action>()));

				// start game clock
				this.setStarted(true);
				this.setStartTime(Instant.now());
				
				// propagate action that game has started to all members of group
				this.propagateAction(new Action(ActionType.START, action.getPlayerId()));

				// start betting rounds
				this.beginBettingRounds();

				// spawning a thread that periodically saves the game
				spawnSaveGameThread();
				
				// spawning a thread that updates player states
				spawnSynchronizationPacketSendingThread();
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
				this.propagateAction(action);
			}
				break;
			case RAISE: {
				Assert.isTrue(this.isStarted(), "Game has not begun");

				Player participant = this.getParticipantById(action.getPlayerId());

				assertIsPlayerTurn(participant);

				// get bet and check that it is valid
				float bet = action.getValue();
				Assert.isTrue(participant.getCurrentBet() + bet > this.getRoundBet(),
						"You have to raise more than the prior bet");

				// update player balances and pot
				participant.bet(bet); // verification performed in 'bet' method
				this.addToPot(bet);

				// update player state to waiting for turn
				participant.setState(PlayerState.WAITING_FOR_TURN);

				// move on to next turn
				this.cycleTurn(this.getGroup().getIndexOfPlayerInList(participant));

				// propagate action to members of group
				this.propagateAction(action);
			}
				break;
			case CALL: {
				Assert.isTrue(this.isStarted(), "Game has not begun");

				Player participant = this.getParticipantById(action.getPlayerId());

				assertIsPlayerTurn(participant);

				// update player balances and pot
				float bet = action.getValue();
				Assert.isTrue(participant.getCurrentBet() + bet == this.getRoundBet(),
						"You have to call with same amount as prior bet");

				// update player balances and pot
				participant.bet(bet); // verification performed in 'bet' method
				this.addToPot(bet);

				// update player state to waiting for turn
				participant.setState(PlayerState.WAITING_FOR_TURN);

				// move on to next turn
				this.cycleTurn(this.getGroup().getIndexOfPlayerInList(participant));

				// propagate action to members of group
				this.propagateAction(action);
			}
				break;
			case CHECK: {
				Assert.isTrue(this.isStarted(), "Game has not begun");

				Player participant = this.getParticipantById(action.getPlayerId());

				assertIsPlayerTurn(participant);
				// update player state to waiting for turn
				participant.setState(PlayerState.WAITING_FOR_TURN);

				// propagate action to members of group
				this.propagateAction(action);
			}
				break;
			case TURN_TIMEOUT: {
				System.out.println("inside turn timeout");
				Player participant = this.getParticipantById(action.getPlayerId());

				assertIsPlayerTurn(participant);
				// we handle timeouts the same way as folding

				// update player state to folded
				participant.setState(PlayerState.FOLDED);

				// move on to next turn
				this.cycleTurn(this.getGroup().getIndexOfPlayerInList(participant));

				// propagate action to members of group
				this.propagateAction(action);
			}
				break;
			case JOIN: {
				// add player as spectator
				this.getGroup().createSpectator(action.getPlayerId(), action.getSession());
				
				System.out.printf("Player %s joining game\n", action.getPlayerId());

				// need to send the player the current game state
				Player participant = this.getParticipantById(action.getPlayerId());
				System.out.println(participant);
				System.out.println(participant.getSession());
				this.sendUserGameState(participant);
			}
				break;
			case SIT: {
				Player participant = this.getParticipantById(action.getPlayerId());
				

				this.getGroup().moveSpectatorToActivePlayer(participant, action.getSlot());
											
				participant.setBalance(action.getValue()); // updating player balance based on buy-in

				System.out.printf("Player %s sitting\n", action.getPlayerId());

				// propagate action to members of group
				this.propagateAction(action);
			}
				break;
			case STAND: {
				Player participant = this.getParticipantById(action.getPlayerId());

				// transition player from player to spectator
				this.getGroup().movePlayerToSpectator(participant);

				// propagate action to members of group
				this.propagateAction(action);
			}
				break;
			case DISCONNECT: {
				Player participant = this.getParticipantById(action.getPlayerId());

				this.getGroup().deleteParticipant(participant);

				// propagate action to members of group if not spectator
				if (participant.isPlaying()) {
					this.propagateAction(action);
				}
			}
				break;
			default:
				throw new Exception("Unsopported action for this game mode");
			}
		} finally {
			// releasing game lock
			this.getLock().unlock();
		}

	}

	/**
	 * Deals new hands to each player
	 * 
	 * @param Boolean
	 *            describing whether to deal all players new hands and clear old
	 *            center cards.
	 */
	@Override
	protected void deal(boolean dealNewHands) {
		// shuffling deck
		deck.shuffle();
		System.out.println("dealing...");
		if (dealNewHands) {
			System.out.println("dealing new hands");
			// deal cards to players
			int numDealt = this.variant.getNumDealt();
			for (Player player : this.getGroup().getPlayers()) {
				player.discardHand(); // discarding any old hands
				for (int i = 0; i < numDealt; i++) {
					player.addCard(deck.dealCard());
				}

				if (numDealt > 0) {
					// sending message to player with new cards
					this.propagateAction(new Action(ActionType.PLAYER_DEAL, player.getId(), player.getCards()));
				}
			}

			this.discardCenterCards();
		}

		// deal center cards
		int newCenterCards = this.variant.getCardsDealtBeforeRound(this.getRound());
		System.out.printf("dealing %d center cards for round %d\n", newCenterCards, this.getRound());
		for (int i = 0; i < newCenterCards; i++) {
			this.addToCenterCards(deck.dealCard());
		}

		// messaging players regarding new center cards
		if (newCenterCards > 0) {
			this.propagateAction(new Action(ActionType.CENTER_DEAL, null, this.getCenterCards()));
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
			if (!(player.getState() == PlayerState.FOLDED || player.getState() == PlayerState.ALL_IN)) {
				count++;
			}
		}
		
		if (count > 1) {
			return false;
		}
		
		return true;
	}
}

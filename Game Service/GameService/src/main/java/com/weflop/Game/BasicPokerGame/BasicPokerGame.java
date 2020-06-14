package com.weflop.Game.BasicPokerGame;

import java.time.Duration;
import java.time.Instant;

import org.springframework.util.Assert;

import com.weflop.Cards.Deck;
import com.weflop.Cards.StandardDeck;
import com.weflop.Evaluation.HandRankEvaluator;
import com.weflop.Game.Action;
import com.weflop.Game.ActionType;
import com.weflop.Game.Player;
import com.weflop.Game.PlayerState;
import com.weflop.Game.AbstractGame;

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
	
	protected BasicPokerGame(float smallBlind, int tableSize, Duration turnDuration, HandRankEvaluator evaluator) {
		super(smallBlind, tableSize, turnDuration, evaluator);
		this.variant = PokerVariants.getStandardHoldem(); // default is hold'em
		this.deck = new StandardDeck(); // default is standard 52 card deck
	}
	
	protected BasicPokerGame(float smallBlind, int tableSize, Duration turnDuration, 
			VariantRepresentation variant, Deck deck, HandRankEvaluator evaluator) {
		super(smallBlind, tableSize, tableSize, turnDuration, evaluator);
		this.variant = variant;
		this.deck = deck;
	}
	
	protected BasicPokerGame(float smallBlind, float bigBlind, int tableSize, 
			Duration turnDuration, HandRankEvaluator evaluator) {
		super(smallBlind, bigBlind, tableSize, turnDuration, evaluator);
		this.variant = PokerVariants.getStandardHoldem(); // default is hold'em
		this.deck = new StandardDeck(); // default is standard 52 card deck
	}
	
	protected BasicPokerGame(float smallBlind, float bigBlind, int tableSize, 
			Duration turnDuration, VariantRepresentation variant, Deck deck, 
			HandRankEvaluator evaluator) {
		super(smallBlind, bigBlind, tableSize, turnDuration, evaluator);
		this.variant = variant;
		this.deck = deck;
	}
	
	/* Overrided methods from abstract superclass */

	@Override
	public void start() throws Exception {
		// we want to prevent concurrent access to most of our game
		// properties while starting up (which we control with the game lock)
		this.getLock().lock();
		
		try {
			Assert.isTrue(this.getGroup().getPlayers().size() >= 2, "A game requires at least two players");
			Assert.isTrue(!this.isStarted(), "Game has already begun");
			
			// start game clock
			this.setStarted(true);
			this.setStartTime(Instant.now());
						
			// beginning round
			this.beginRound();
		} finally {
			// releasing game lock
			this.getLock().unlock();
		}
	}

	@Override
	public void end() {
		// TODO Auto-generated method stub

	}

	@Override
	public void performAction(long participantID, Action action) throws Exception {
		// locking game object
		this.getLock().lock();

		try {
			Player participant = this.getParticipantById(participantID);
			
			switch(action.getType()) {
				case FOLD:
				{
					assertIsPlayerTurn(participant);
					
					// update player state to folded
					participant.setState(PlayerState.FOLDED);
					
					// move on to next turn
					this.cycleTurn(this.getGroup().getIndexOfPlayerInList(participant));
					// TODO: propogate updates
				}
					break;
				case RAISE:
				{
					assertIsPlayerTurn(participant);
					
					// get bet and check that it is valid
					float bet = action.getValue();
					Assert.isTrue(participant.getCurrentBet() + bet > this.getRoundBet(), "You have to raise more than the prior bet");
					
					// update player balances and pot
					participant.bet(bet); // verification performed in 'bet' method
					this.addToPot(bet);
					
					// update player state to waiting for turn
					participant.setState(PlayerState.WAITING_FOR_TURN);
					
					// move on to next turn
					this.cycleTurn(this.getGroup().getIndexOfPlayerInList(participant));
					// TODO: propogate updates
				}
					break;
				case CALL:
				{
					assertIsPlayerTurn(participant);
					
					// update player balances and pot
					float bet = action.getValue();
					Assert.isTrue(participant.getCurrentBet() + bet == this.getRoundBet(), "You have to call with same amount as prior bet");
					
					// update player balances and pot
					participant.bet(bet); // verification performed in 'bet' method
					this.addToPot(bet);

					// update player state to waiting for turn
					participant.setState(PlayerState.WAITING_FOR_TURN);
					
					// move on to next turn
					this.cycleTurn(this.getGroup().getIndexOfPlayerInList(participant));
					// TODO: propogate updates
				}
					break;
				case CHECK:
				{
					assertIsPlayerTurn(participant);
					// update player state to waiting for turn
					participant.setState(PlayerState.WAITING_FOR_TURN);
				}
					break;
				case TURN_TIMEOUT:
				{
					// we handle timeouts the same way as folding
					this.getLock().unlock();
					try {
						this.performAction(participantID, new Action(ActionType.FOLD));
					} finally {
						this.getLock().lock();
					}
				}
					break;
				case SIT:
				{
					
				}
					break;
				case EXIT:
				{
					
				}
					break;
				case DISCONNECT:
				{
					
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

	@Override
	public void sendGamePackets() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void flushToDatabase() throws Exception {
		// TODO Auto-generated method stub

	}
	
	/**
	 * Deals new hands to each player
	 */
	@Override
	protected void deal() {
		// shuffling deck
		deck.shuffle();

		// deal cards to players
		int numDealt = this.variant.getNumDealt();
		for (Player player : this.getGroup().getPlayers()) {
			player.discardHand(); // discarding any old hands
			for (int i=0; i < numDealt; i++) {
				player.addCard(deck.dealCard());
			}
		}
	}
}

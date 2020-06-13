package com.weflop.Game.BasicPokerGame;

import java.time.Duration;
import java.time.Instant;

import com.weflop.Cards.Deck;
import com.weflop.Cards.StandardDeck;
import com.weflop.Game.Action;
import com.weflop.Game.Player;
import com.weflop.Game.PlayerState;
import com.weflop.Game.Turn;
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
	
	protected BasicPokerGame(float smallBlind, int tableSize, Duration turnDuration) {
		super(smallBlind, tableSize, turnDuration);
		this.variant = PokerVariants.getStandardHoldem(); // default is hold'em
		this.deck = new StandardDeck(); // default is standard 52 card deck
	}
	
	protected BasicPokerGame(float smallBlind, int tableSize, Duration turnDuration, 
			VariantRepresentation variant, Deck deck) {
		super(smallBlind, tableSize, tableSize, turnDuration);
		this.variant = variant;
		this.deck = deck;
	}
	
	protected BasicPokerGame(float smallBlind, float bigBlind, int tableSize, Duration turnDuration) {
		super(smallBlind, bigBlind, tableSize, turnDuration);
		this.variant = PokerVariants.getStandardHoldem(); // default is hold'em
		this.deck = new StandardDeck(); // default is standard 52 card deck
	}
	
	protected BasicPokerGame(float smallBlind, float bigBlind, int tableSize, 
			Duration turnDuration, VariantRepresentation variant, Deck deck) {
		super(smallBlind, bigBlind, tableSize, turnDuration);
		this.variant = variant;
		this.deck = deck;
	}

	@Override
	public void start() throws Exception {
		// we want to prevent concurrent access to most of our game
		// properties while starting up (which we control with the game lock)
		this.getLock().lock();
		
		try {
			assert this.getGroup().getPlayers().length >= 2 : "A game requires at least two players";
			assert !this.isStarted() : "Game has already begun";
			
			// start game clock
			this.setStarted(true);
			this.setStartTime(Instant.now());
			
			// shuffling deck
			deck.shuffle();
			
			// deal cards to players
			int numDealt = this.variant.getNumDealt();
			for (Player player : this.getGroup().getPlayers()) {
				for (int i=0; i < numDealt; i++) {
					player.addCard(deck.dealCard());
				}
			}
			
			// initializing turn for dealer
			Player dealer = this.getGroup().getPlayers()[this.getDealerIndex()];
			dealer.setState(PlayerState.CURRENT_TURN);
			
			this.setTurn(new Turn(dealer, System.nanoTime()));
			
			// beginning turn by starting timer
			this.beginRound();
		} finally {
			// releasing game lock
			this.getLock().unlock();
		}
	}

	@Override
	public void end() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void performAction(long playerID, Action action) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendGamePackets() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void flushToDatabase() throws Exception {
		// TODO Auto-generated method stub

	}

}

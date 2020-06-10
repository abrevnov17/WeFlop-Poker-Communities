package com.weflop.Game.BasicPokerGame;

import com.weflop.Game.Action;
import com.weflop.Game.Game;

/**
 * This class represents an actual Poker game following the 
 * 
 * @author abrevnov
 *
 */
public class BasicPokerGame extends Game {

	VariantRepresentation variant;
	
	protected BasicPokerGame(float smallBlind) {
		super(smallBlind);
		this.variant = PokerVariants.getStandardHoldem(); // default is hold'em
	}
	
	protected BasicPokerGame(float smallBlind, VariantRepresentation variant) {
		super(smallBlind);
		this.variant = variant;
	}
	
	protected BasicPokerGame(float smallBlind, float bigBlind) {
		super(smallBlind, bigBlind);
		this.variant = PokerVariants.getStandardHoldem(); // default is hold'em
	}
	
	protected BasicPokerGame(float smallBlind, float bigBlind, VariantRepresentation variant) {
		super(smallBlind, bigBlind);
		this.variant = variant;
	}

	@Override
	public void start() throws Exception {
		// TODO Auto-generated method stub
 
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
	public void sendPackets() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void flushToDatabase() throws Exception {
		// TODO Auto-generated method stub

	}

}

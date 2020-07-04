package com.weflop.Game.BasicPokerGame;

/**
 * Describes a representation of a standard Poker variant
 * of the form 'a|b|c|d e f g...|h i j k...'. 
 * 
 * In the above form:
 * 'a' represents the number of cards dealt. 'b' represents the number
 * of betting rounds. 'c' represents the number of hole cards required 
 * to form the best five card hand. 'd e f g...' represent the number cards 
 * dealt before each betting round. 'h i j k' represents the number of cards
 * discarded after each round of betting.
 * 
 * @author abrevnov
 *
 */
public class VariantRepresentation {
	
	private int numDealt;
	private int bettingRounds;
	
	private int[] cardsDealtBeforeRound;
	private int holeCards;
	
	private int[] cardsDiscardedAfterRound;

	
	VariantRepresentation(int numDealt, int bettingRounds, int holeCards, int[] cardsDealtBeforeRound, int[] cardsDiscardedAfterRound) {
		this.setNumDealt(numDealt);
		this.setBettingRounds(bettingRounds);
		this.cardsDealtBeforeRound = cardsDealtBeforeRound;
		this.setHoleCards(holeCards);
		this.cardsDealtBeforeRound = cardsDiscardedAfterRound;
	}
	
	public int getCardDealtBeforeRound(int round) {
		return this.cardsDealtBeforeRound[round];
	}
	
	public int getCardDiscardedAfterRound(int round) {
		return this.cardsDiscardedAfterRound[round];
	}

	public int getNumDealt() {
		return numDealt;
	}

	public void setNumDealt(int numDealt) {
		this.numDealt = numDealt;
	}

	public int getBettingRounds() {
		return bettingRounds;
	}

	public void setBettingRounds(int bettingRounds) {
		this.bettingRounds = bettingRounds;
	}

	public int getHoleCards() {
		return holeCards;
	}

	public void setHoleCards(int holeCards) {
		this.holeCards = holeCards;
	}
}

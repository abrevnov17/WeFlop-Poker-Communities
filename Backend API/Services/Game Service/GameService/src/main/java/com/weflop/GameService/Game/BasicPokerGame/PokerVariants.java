package com.weflop.GameService.Game.BasicPokerGame;

/**
 * Contains static methods that are used to get representations of poker
 * 
 * @author abrevnov
 *
 */
public class PokerVariants {

	/**
	 * Gets standard represent of a standard Texas-Holdem game.
	 * 
	 * @return A representation of the game.
	 */
	public static VariantRepresentation getStandardHoldem() {
		int[] cardsDealtBeforeRound = new int[] { 0, 3, 1, 1 };
		int[] cardsDiscardedAfterRound = new int[] { 0, 0, 0, 0 };
		return new VariantRepresentation(2, 4, 0, cardsDealtBeforeRound, cardsDiscardedAfterRound);
	}
}

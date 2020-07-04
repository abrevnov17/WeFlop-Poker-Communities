package com.weflop.Cards;

/**
 * Interface specifies methods that any deck
 * (even non-standard decks) must implement.
 * 
 * @author abrevnov
 *
 */
public interface Deck {
	/**
	 * Shuffles the deck
	 */
	public void shuffle();

	/**
	 * Get number of cards left in the deck
	 * 
	 * @return Number of cards left in the deck
	 */
	public int cardsLeft();

	/**
	 * Deals the next card from the deck
	 * 
	 * @return The next card
	 */
	public Card dealCard();
}

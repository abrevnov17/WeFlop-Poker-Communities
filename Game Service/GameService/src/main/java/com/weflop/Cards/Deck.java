package com.weflop.Cards;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

/**
 * Deck.java
 * 
 * Instance of Deck represents a standard 52-card deck
 * 
 * @author abrevnov
 *
 */
public class Deck {

	private List<Card> deck;   // an array of 52 Cards that form our deck.
	private int numDealt; // number of cards that have been dealt

	// we cache the suit and card values so we can easily convert from integer
	// to suit and card enum values
	private final Suit[] suitInts = Suit.values();
	private final CardValue[] cardValueInts = CardValue.values();

	public Deck() {		
		// starting out with no cards dealt (i.e. a full d
		this.numDealt = 0;

		// initialize an UNSHUFFLED deck of cards
		deck = new ArrayList<Card>();
		for (int suitInt = 0; suitInt <= 3; suitInt++) {
			for (int cardValueInt = 1; cardValueInt <= 13; cardValueInt++) {
				deck.add(new Card(suitInts[cardValueInt],cardValueInts[cardValueInt]));
			}
		}
	}


	/**
	 * Shuffles the deck
	 */
	public void shuffle() {
		// making the deck "full" again
		this.numDealt = 0;

		// shuffling the deck
		Collections.shuffle(this.deck);
	}

	/**
	 * Get number of cards left in the deck
	 * 
	 * @return Number of cards left in the deck
	 */
	public int cardsLeft() {
		return 52 - this.numDealt;
	}

	/**
	 * Deals the next card from the deck
	 * 
	 * @return The next card
	 */
	public Card dealCard() {
		// Deals one card from the deck and returns it.

		// Once deck is empty, automatically re-shuffles (this 
		// feature may not be that useful for our purposes).
		if (numDealt == 52) {
			this.shuffle();
		}
		this.numDealt++;
		return this.deck.get(numDealt-1);
	}

}

// Adapted from open-source hand-evaluator. Original license shown below:

/*
The MIT License (MIT)

Copyright (c) 2013 Jacob Kanipe-Illig

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
*/
package com.weflop.Evaluation.TwoPlusTwo;

import java.util.List;

import com.weflop.Cards.Card;
import com.weflop.Evaluation.EvaluationHandsLoader;
import com.weflop.Evaluation.HandRank;
import com.weflop.Evaluation.HandRankEvaluator;

/**
 * Poker hand Evaluation algorithm based on the two plus two 7 card hand evaluation algorithm.
 * 
 * @author jacobhyphenated
 */
public class TwoPlusTwoHandEvaluator implements HandRankEvaluator {

	private static final String HAND_RANKS = "/HandRanks.dat";
	private int[] handRanks;
	
	private static TwoPlusTwoHandEvaluator instance;
	
	public TwoPlusTwoHandEvaluator(){
		EvaluationHandsLoader reader = new EvaluationHandsLoader();
		handRanks = reader.loadHandRankResource(HAND_RANKS);
	}
	
	/**
	 * The two plus two lookup table is very memory intensive.  You should only ever create
	 * one instance of the class.  Use this method to keep the singleton pattern.
	 * @return {@link TwoPlusTwoHandEvaluator} instance
	 */
	public static TwoPlusTwoHandEvaluator getInstance(){
		if(instance == null){
			instance = new TwoPlusTwoHandEvaluator();
		}
		return instance;
	}
	
	@Override
	public HandRank evaluate(List<Card> board, List<Card> hand) {
		board.addAll(hand); // concatenating all cards
		
		int p = 53;
		for (Card card : board) {
			p = handRanks[p + cardToIntegerRepresentation(card)];
		}
		return new HandRank(p);
	}
	
	/**
	 * 
	 * Helper method that takes in a given card and returns its integer
	 * representation (as used in the TwoPlusTwo algorithm). This representation
	 * is as follows:
	 * 
	 * "2c": 1,
	 * "2d": 2,
	 * "2h": 3,
	 * "2s": 4,
	 * "3c": 5,
	 * "3d": 6,
	 * ...
	 * "kh": 47,
	 * "ks": 48,
	 * "ac": 49,
	 * "ad": 50,
	 * "ah": 51,
	 * "as": 52
	 * 
	 * @param card
	 * @return An integer encoding the card.
	 */
	private int cardToIntegerRepresentation(Card card) {
		int result = 0;
		switch (card.getSuit()) {
		case CLUBS:
			result = result + 1;
			break;
		case DIAMONDS:
			result = result + 2;
			break;
		case HEARTS:
			result = result + 3;
			break;
		case SPADES:
			result = result + 4;
			break;
		default:
			System.out.println("ERROR: Invalid card");
			break;
		}
		
		switch (card.getCardValue()) {
		case TWO:
			break;
		case THREE:
			result = result + 4;
			break;
		case FOUR:
			result = result + 8;
			break;
		case FIVE:
			result = result + 12;
			break;
		case SIX:
			result = result + 16;
			break;
		case SEVEN:
			result = result + 20;
			break;
		case EIGHT:
			result = result + 24;
			break;
		case NINE:
			result = result + 28;
			break;
		case TEN:
			result = result + 32;
			break;
		case JACK:
			result = result + 36;
			break;
		case QUEEN:
			result = result + 40;
			break;
		case KING:
			result = result + 44;
			break;
		case ACE:
			result = result + 48;
			break;
		default:
			System.out.println("ERROR: Invalid card");
			break;
		}
		
		return result;
	}

}
package com.weflop.Evaluation;

import java.util.List;

import com.weflop.Cards.Card;

/**
 * HandRankEvaluator.java
 * 
 * Interface requiring implementing classes to, given table cards and a users
 * hand, to evaluate the hand.
 * 
 * @author abrevnov
 *
 */
public interface HandRankEvaluator {
	public HandRank evaluate(List<Card> tableCards, List<Card> hand);
}

package com.weflop.Evaluation;

import com.weflop.Cards.Board;
import com.weflop.Cards.Hand;

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
	public HandRank evaluate(Board tableCards, Hand hand);
}

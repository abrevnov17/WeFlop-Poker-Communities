package com.weflop.Evaluation.TwoPlusTwo;

import com.weflop.Evaluation.HandClassification;
import com.weflop.Evaluation.HandRank;

/**
 * TwoPlusTwoHandRank.java
 * 
 * Extends our HandRank abstract class and implements method that yields the
 * classification of the given hand.
 * 
 * @author abrevnov
 *
 */
public class TwoPlusTwoHandRank extends HandRank {
	public TwoPlusTwoHandRank(int rankValue) {
		super(rankValue);
	}

	@Override
	public HandClassification getHandType() {
		/*
		 * The rank value is categorized based on the Two Plus Two (2+2) hand evaluation
		 * algorithm. 0 == invalid hand, 1 == high card, so on. This value is stored in
		 * the thirteenth bit of the rankValue score. Bit-shift to get the type value
		 * bit, match it to the enum with the ordinal ordering for the 2+2 evaluation
		 * type
		 */
		return HandClassification.values()[rankValue >> 12];
	}

}

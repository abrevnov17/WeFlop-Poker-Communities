package com.weflop.Evaluation.TwoPlusTwo;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.weflop.Cards.Card;
import com.weflop.Cards.CardValue;
import com.weflop.Cards.Suit;
import com.weflop.Evaluation.HandClassification;
import com.weflop.Evaluation.HandRank;
import com.weflop.Evaluation.HandRankEvaluator;

public class TwoPlusTwoEvaluatorTests {
	
	private final HandRankEvaluator[] EVALUATORS = 
		{ 
			TwoPlusTwoHandEvaluator.getInstance() 
		};
	
	@Test
    public void highCardWins() {
        List<Card> hand1 = new ArrayList<Card>();
        hand1.add(new Card(Suit.CLUBS, CardValue.KING));
        hand1.add(new Card(Suit.HEARTS, CardValue.THREE));


        List<Card> hand2 = new ArrayList<Card>();
        hand2.add(new Card(Suit.CLUBS, CardValue.ACE));
        hand2.add(new Card(Suit.HEARTS, CardValue.TWO));
        
        List<Card> table = new ArrayList<Card>();
        table.add(new Card(Suit.DIAMONDS, CardValue.EIGHT));
        table.add(new Card(Suit.DIAMONDS, CardValue.NINE));
        table.add(new Card(Suit.CLUBS, CardValue.FOUR));
        table.add(new Card(Suit.HEARTS, CardValue.SIX));
        table.add(new Card(Suit.HEARTS, CardValue.JACK));
        
        for (HandRankEvaluator evaluator : EVALUATORS) {
        	assertTrue(compareAndAssertHandTypes(evaluator, hand1, 
        			HandClassification.HIGH_CARD, hand2, HandClassification.HIGH_CARD, table) < 0);
        }
    }
	
	@Test
    public void pairBeatsHighCard() {
        List<Card> hand1 = new ArrayList<Card>();
        hand1.add(new Card(Suit.CLUBS, CardValue.TWO));
        hand1.add(new Card(Suit.HEARTS, CardValue.TWO));


        List<Card> hand2 = new ArrayList<Card>();
        hand2.add(new Card(Suit.CLUBS, CardValue.KING));
        hand2.add(new Card(Suit.HEARTS, CardValue.ACE));
        
        List<Card> table = new ArrayList<Card>();
        table.add(new Card(Suit.DIAMONDS, CardValue.EIGHT));
        table.add(new Card(Suit.DIAMONDS, CardValue.NINE));
        table.add(new Card(Suit.CLUBS, CardValue.FOUR));
        table.add(new Card(Suit.HEARTS, CardValue.SIX));
        table.add(new Card(Suit.HEARTS, CardValue.THREE));
        
        for (HandRankEvaluator evaluator : EVALUATORS) {
        	assertTrue(compareAndAssertHandTypes(evaluator, hand1, 
        			HandClassification.PAIR, hand2, HandClassification.HIGH_CARD, table) > 0);
        }
	}

	@Test
    public void higherPairWins() {
        List<Card> hand1 = new ArrayList<Card>();
        hand1.add(new Card(Suit.CLUBS, CardValue.ACE));
        hand1.add(new Card(Suit.HEARTS, CardValue.ACE));


        List<Card> hand2 = new ArrayList<Card>();
        hand2.add(new Card(Suit.CLUBS, CardValue.KING));
        hand2.add(new Card(Suit.HEARTS, CardValue.KING));
        
        List<Card> table = new ArrayList<Card>();
        table.add(new Card(Suit.DIAMONDS, CardValue.EIGHT));
        table.add(new Card(Suit.DIAMONDS, CardValue.NINE));
        table.add(new Card(Suit.CLUBS, CardValue.FOUR));
        table.add(new Card(Suit.HEARTS, CardValue.SIX));
        table.add(new Card(Suit.HEARTS, CardValue.JACK));
        
        for (HandRankEvaluator evaluator : EVALUATORS) {
        	assertTrue(compareAndAssertHandTypes(evaluator, hand1, 
        			HandClassification.PAIR, hand2, HandClassification.PAIR, table) > 0);
        }
    }
	
	@Test
    public void twoPairBeatsSinglePair() {
        List<Card> hand1 = new ArrayList<Card>();
        hand1.add(new Card(Suit.CLUBS, CardValue.ACE));
        hand1.add(new Card(Suit.HEARTS, CardValue.ACE));


        List<Card> hand2 = new ArrayList<Card>();
        hand2.add(new Card(Suit.CLUBS, CardValue.EIGHT));
        hand2.add(new Card(Suit.HEARTS, CardValue.NINE));
        
        List<Card> table = new ArrayList<Card>();
        table.add(new Card(Suit.DIAMONDS, CardValue.EIGHT));
        table.add(new Card(Suit.DIAMONDS, CardValue.NINE));
        table.add(new Card(Suit.CLUBS, CardValue.FOUR));
        table.add(new Card(Suit.HEARTS, CardValue.SIX));
        table.add(new Card(Suit.HEARTS, CardValue.JACK));
        
        for (HandRankEvaluator evaluator : EVALUATORS) {
        	assertTrue(compareAndAssertHandTypes(evaluator, hand1, 
        			HandClassification.PAIR, hand2, HandClassification.TWO_PAIR, table) < 0);
        }
	}
	
	@Test
    public void threeOfAKindBeatsTwoPair() {
        List<Card> hand1 = new ArrayList<Card>();
        hand1.add(new Card(Suit.CLUBS, CardValue.JACK));
        hand1.add(new Card(Suit.DIAMONDS, CardValue.JACK));


        List<Card> hand2 = new ArrayList<Card>();
        hand2.add(new Card(Suit.CLUBS, CardValue.EIGHT));
        hand2.add(new Card(Suit.HEARTS, CardValue.NINE));
        
        List<Card> table = new ArrayList<Card>();
        table.add(new Card(Suit.DIAMONDS, CardValue.EIGHT));
        table.add(new Card(Suit.DIAMONDS, CardValue.NINE));
        table.add(new Card(Suit.CLUBS, CardValue.FOUR));
        table.add(new Card(Suit.HEARTS, CardValue.SIX));
        table.add(new Card(Suit.HEARTS, CardValue.JACK));
        
        for (HandRankEvaluator evaluator : EVALUATORS) {
	        assertTrue(compareAndAssertHandTypes(evaluator, hand1, 
	    			HandClassification.THREE_OF_A_KIND, hand2, HandClassification.TWO_PAIR, table) > 0);
        }
	}
	
	@Test
    public void straightBeatsThreeOfAKind() {
        List<Card> hand1 = new ArrayList<Card>();
        hand1.add(new Card(Suit.CLUBS, CardValue.JACK));
        hand1.add(new Card(Suit.DIAMONDS, CardValue.JACK));


        List<Card> hand2 = new ArrayList<Card>();
        hand2.add(new Card(Suit.CLUBS, CardValue.TEN));
        hand2.add(new Card(Suit.HEARTS, CardValue.QUEEN));
        
        List<Card> table = new ArrayList<Card>();
        table.add(new Card(Suit.DIAMONDS, CardValue.EIGHT));
        table.add(new Card(Suit.DIAMONDS, CardValue.NINE));
        table.add(new Card(Suit.CLUBS, CardValue.FOUR));
        table.add(new Card(Suit.HEARTS, CardValue.SIX));
        table.add(new Card(Suit.HEARTS, CardValue.JACK));
        
        for (HandRankEvaluator evaluator : EVALUATORS) {
	        assertTrue(compareAndAssertHandTypes(evaluator, hand1, 
	    			HandClassification.THREE_OF_A_KIND, hand2, HandClassification.STRAIGHT, table) < 0);
        }
	}
	
	@Test
    public void flushBeatsStraight() {
        List<Card> hand1 = new ArrayList<Card>();
        hand1.add(new Card(Suit.HEARTS, CardValue.TWO));
        hand1.add(new Card(Suit.HEARTS, CardValue.THREE));


        List<Card> hand2 = new ArrayList<Card>();
        hand2.add(new Card(Suit.CLUBS, CardValue.TEN));
        hand2.add(new Card(Suit.HEARTS, CardValue.QUEEN));
        
        List<Card> table = new ArrayList<Card>();
        table.add(new Card(Suit.HEARTS, CardValue.EIGHT));
        table.add(new Card(Suit.DIAMONDS, CardValue.NINE));
        table.add(new Card(Suit.CLUBS, CardValue.FOUR));
        table.add(new Card(Suit.HEARTS, CardValue.SIX));
        table.add(new Card(Suit.HEARTS, CardValue.JACK));
        
        for (HandRankEvaluator evaluator : EVALUATORS) {
	        assertTrue(compareAndAssertHandTypes(evaluator, hand1, 
	    			HandClassification.FLUSH, hand2, HandClassification.STRAIGHT, table) > 0);
        }
	}
	
	@Test
    public void fullHouseBeatsFlush() {
        List<Card> hand1 = new ArrayList<Card>();
        hand1.add(new Card(Suit.HEARTS, CardValue.TWO));
        hand1.add(new Card(Suit.HEARTS, CardValue.THREE));


        List<Card> hand2 = new ArrayList<Card>();
        hand2.add(new Card(Suit.DIAMONDS, CardValue.SIX));
        hand2.add(new Card(Suit.HEARTS, CardValue.NINE));
        
        List<Card> table = new ArrayList<Card>();
        table.add(new Card(Suit.HEARTS, CardValue.EIGHT));
        table.add(new Card(Suit.DIAMONDS, CardValue.NINE));
        table.add(new Card(Suit.CLUBS, CardValue.NINE));
        table.add(new Card(Suit.HEARTS, CardValue.SIX));
        table.add(new Card(Suit.HEARTS, CardValue.JACK));
        
        for (HandRankEvaluator evaluator : EVALUATORS) {
	        assertTrue(compareAndAssertHandTypes(evaluator, hand1, 
	    			HandClassification.FLUSH, hand2, HandClassification.FULL_HOUSE, table) < 0);
        }
	}
	
	@Test
    public void fourOfAKindBeatsFullHouse() {
        List<Card> hand1 = new ArrayList<Card>();
        hand1.add(new Card(Suit.HEARTS, CardValue.NINE));
        hand1.add(new Card(Suit.SPADES, CardValue.NINE));


        List<Card> hand2 = new ArrayList<Card>();
        hand2.add(new Card(Suit.DIAMONDS, CardValue.SIX));
        hand2.add(new Card(Suit.HEARTS, CardValue.NINE));
        
        List<Card> table = new ArrayList<Card>();
        table.add(new Card(Suit.HEARTS, CardValue.EIGHT));
        table.add(new Card(Suit.DIAMONDS, CardValue.NINE));
        table.add(new Card(Suit.CLUBS, CardValue.NINE));
        table.add(new Card(Suit.HEARTS, CardValue.SIX));
        table.add(new Card(Suit.HEARTS, CardValue.JACK));
        
        for (HandRankEvaluator evaluator : EVALUATORS) {
	        assertTrue(compareAndAssertHandTypes(evaluator, hand1, 
	    			HandClassification.FOUR_OF_A_KIND, hand2, HandClassification.FULL_HOUSE, table) > 0);
        }
	}
	
	@Test
    public void straightFlushBeatsFourOfAKind() {
        List<Card> hand1 = new ArrayList<Card>();
        hand1.add(new Card(Suit.DIAMONDS, CardValue.NINE));
        hand1.add(new Card(Suit.SPADES, CardValue.NINE));


        List<Card> hand2 = new ArrayList<Card>();
        hand2.add(new Card(Suit.HEARTS, CardValue.SEVEN));
        hand2.add(new Card(Suit.HEARTS, CardValue.TEN));
        
        List<Card> table = new ArrayList<Card>();
        table.add(new Card(Suit.HEARTS, CardValue.EIGHT));
        table.add(new Card(Suit.HEARTS, CardValue.NINE));
        table.add(new Card(Suit.CLUBS, CardValue.NINE));
        table.add(new Card(Suit.HEARTS, CardValue.SIX));
        table.add(new Card(Suit.SPADES, CardValue.TEN));
        
        for (HandRankEvaluator evaluator : EVALUATORS) {
	        assertTrue(compareAndAssertHandTypes(evaluator, hand1, 
	    			HandClassification.FOUR_OF_A_KIND, hand2, HandClassification.STRAIGHT_FLUSH, table) < 0);
        }
	}
	
	@Test
    public void royalFlushBeatsStraightFlush() {
        List<Card> hand1 = new ArrayList<Card>();
        hand1.add(new Card(Suit.HEARTS, CardValue.ACE));
        hand1.add(new Card(Suit.SPADES, CardValue.FOUR));


        List<Card> hand2 = new ArrayList<Card>();
        hand2.add(new Card(Suit.HEARTS, CardValue.THREE));
        hand2.add(new Card(Suit.HEARTS, CardValue.NINE));
        
        List<Card> table = new ArrayList<Card>();
        table.add(new Card(Suit.DIAMONDS, CardValue.FOUR));
        table.add(new Card(Suit.HEARTS, CardValue.TEN));
        table.add(new Card(Suit.HEARTS, CardValue.JACK));
        table.add(new Card(Suit.HEARTS, CardValue.QUEEN));
        table.add(new Card(Suit.HEARTS, CardValue.KING));
        
        for (HandRankEvaluator evaluator : EVALUATORS) {
	        assertTrue(compareAndAssertHandTypes(evaluator, hand1, 
	    			HandClassification.STRAIGHT_FLUSH, hand2, HandClassification.STRAIGHT_FLUSH, table) > 0);
        }
	}
	
	@Test
    public void equalRankingsAllowed() {
        List<Card> hand1 = new ArrayList<Card>();
        hand1.add(new Card(Suit.HEARTS, CardValue.FOUR));
        hand1.add(new Card(Suit.SPADES, CardValue.FIVE));


        List<Card> hand2 = new ArrayList<Card>();
        hand2.add(new Card(Suit.CLUBS, CardValue.FIVE));
        hand2.add(new Card(Suit.DIAMONDS, CardValue.TWO));
        
        List<Card> table = new ArrayList<Card>();
        table.add(new Card(Suit.DIAMONDS, CardValue.TEN));
        table.add(new Card(Suit.SPADES, CardValue.JACK));
        table.add(new Card(Suit.CLUBS, CardValue.SEVEN));
        table.add(new Card(Suit.SPADES, CardValue.ACE));
        table.add(new Card(Suit.HEARTS, CardValue.KING));
        
        for (HandRankEvaluator evaluator : EVALUATORS) {
	        assertTrue(compareAndAssertHandTypes(evaluator, hand1, 
	    			HandClassification.HIGH_CARD, hand2, HandClassification.HIGH_CARD, table) == 0);
        }
	}
	
	private int compareAndAssertHandTypes(HandRankEvaluator evaluator, List<Card> h1, HandClassification type1, 
			List<Card> h2, HandClassification type2, List<Card> table){
		HandRank rank1 = evaluator.evaluate(table, h1);
		HandRank rank2 = evaluator.evaluate(table, h2);
		assertTrue(rank1.getHandType() == type1, String.format("Expected: %s, got: %s\n", type1.toString(), rank1.getHandType().toString()));
		assertTrue(rank2.getHandType() == type2, String.format("Expected: %s, got: %s\n", type2.toString(), rank2.getHandType().toString()));
		return rank1.compareTo(rank2);
	}
}

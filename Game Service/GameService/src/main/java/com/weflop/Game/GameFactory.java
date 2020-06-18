package com.weflop.Game;

import java.time.Duration;

import com.weflop.Evaluation.TwoPlusTwo.TwoPlusTwoHandEvaluator;
import com.weflop.Game.BasicPokerGame.BasicPokerGame;

/**
 * Factory generates instances of Games.
 * 
 * @author abrevnov
 *
 */
public class GameFactory {
	
	/**
	 * Creates a standard poker game.
	 * 
	 * @return
	 */
	public static Game generateStandardPokerGame(float smallBlind) {
		return new BasicPokerGame(smallBlind, 8, Duration.ofSeconds(180),  new TwoPlusTwoHandEvaluator());
	}
}

package com.weflop.Game;

import com.weflop.Evaluation.TwoPlusTwo.TwoPlusTwoHandEvaluator;
import com.weflop.Game.BasicPokerGame.BasicPokerGame;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Factory generates instances of Games.
 * 
 * @author abrevnov
 *
 */
public class GameFactory {

	public static Map<String, Game> ID_TO_GAME = new ConcurrentHashMap<String, Game>();

	/**
	 * Creates a standard poker game and adds game to map of id's to games.
	 * 
	 * @return Id of newly created game
	 */
	public static String generateStandardPokerGame(String name, float smallBlind, int minBuyInBB, int maxBuyInBB, String createdBy) {
		GameCustomMetadata metadata = new GameCustomMetadata(name, smallBlind, minBuyInBB, maxBuyInBB, createdBy);
		Game game = new BasicPokerGame(metadata, new TwoPlusTwoHandEvaluator());
		ID_TO_GAME.put(game.getGameId(), game);
		return game.getGameId();
	}
}

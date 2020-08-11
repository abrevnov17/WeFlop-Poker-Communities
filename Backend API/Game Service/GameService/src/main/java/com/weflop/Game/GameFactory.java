package com.weflop.Game;

import com.weflop.Evaluation.TwoPlusTwo.TwoPlusTwoHandEvaluator;

import com.weflop.Game.BasicPokerGame.BasicPokerGame;
import com.weflop.GameService.Database.DomainObjects.GameDocument;

/**
 * Factory generates instances of Games.
 * 
 * @author abrevnov
 *
 */
public class GameFactory {

	/**
	 * Creates a standard poker game and adds game to map of id's to games.
	 * 
	 * @return Id of newly created game
	 */
	public static String generateStandardPokerGame(String name, float smallBlind, int minBuyInBB, int maxBuyInBB, String createdBy) {
		GameCustomMetadata metadata = new GameCustomMetadata(name, smallBlind, minBuyInBB, maxBuyInBB, createdBy);
		Game game = new BasicPokerGame(metadata, new TwoPlusTwoHandEvaluator());
		GameManager.ID_TO_GAME.put(game.getGameId(), game);
		return game.getGameId();
	}
	
	/**
	 * Loads a poker game from a game document.
	 * @param document
	 * @return Corresponding poker game instance
	 */
	public static Game fromDocument(GameDocument document) {
		switch(GameType.fromValue(document.getType())) {
			case STANDARD_REPRESENTATION: {
				Game game = new BasicPokerGame(document, new TwoPlusTwoHandEvaluator());
				GameManager.ID_TO_GAME.put(game.getGameId(), game);
				return game;
			}
		}
		
		throw new RuntimeException("Invalid game document");
	}
}

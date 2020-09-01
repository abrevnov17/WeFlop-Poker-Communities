package com.weflop.GameService.Game;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.weflop.Evaluation.TwoPlusTwo.TwoPlusTwoHandEvaluator;
import com.weflop.GameService.Database.GameRepository;
import com.weflop.GameService.Database.DomainObjects.GameDocument;
import com.weflop.GameService.Game.BasicPokerGame.BasicPokerGame;

/**
 * Factory generates instances of Games.
 * 
 * @author abrevnov
 *
 */
@Component
public class GameFactory {
	
	@Autowired
	private GameRepository repository;

	/**
	 * Creates a standard poker game and adds game to map of id's to games.
	 * 
	 * @return Id of newly created game
	 */
	public String generateStandardPokerGame(String name, float smallBlind, int minBuyInBB, int maxBuyInBB, String createdBy) {
		GameCustomMetadata metadata = new GameCustomMetadata(name, smallBlind, minBuyInBB, maxBuyInBB, createdBy);
		Game game = new BasicPokerGame(repository, metadata, new TwoPlusTwoHandEvaluator());
		GameManager.ID_TO_GAME.put(game.getGameId(), game);
		return game.getGameId();
	}
	
	/**
	 * Loads a poker game from a game document.
	 * @param document
	 * @return Corresponding poker game instance
	 */
	public Game fromDocument(GameDocument document) {
		switch(GameType.fromValue(document.getType())) {
			case STANDARD_REPRESENTATION: {
				Game game = new BasicPokerGame(repository, document, new TwoPlusTwoHandEvaluator());
				GameManager.ID_TO_GAME.put(game.getGameId(), game);
				return game;
			}
		}
		
		throw new RuntimeException("Invalid game document");
	}
}

package com.weflop.REST;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.weflop.Game.Game;
import com.weflop.Game.GameCustomMetadata;
import com.weflop.Game.GameFactory;

@Controller
public class RESTController {

	@PostMapping("/create-game")
	@ResponseBody
	public String createGame(@RequestParam(name = "user_id", required = true) String userId,
			@RequestParam(name = "small_blind", required = true) float smallBlind) {

		if (smallBlind < 0.5) {
			throw new IllegalArgumentException("Small blind must be greater than or equal to 0.5");
		}

		return GameFactory.generateStandardPokerGame(userId, smallBlind); // returning id of newly created game
	}

	@GetMapping("/game-metadata")
	@ResponseBody
	public GameCustomMetadata getGameMetadata(@RequestParam(name = "game_id", required = true) String gameId) {
		Game game = GameFactory.ID_TO_GAME.get(gameId);
		if (game == null) {
			throw new IllegalArgumentException("No game exists with id: " + gameId);
		}

		return game.getGameMetadata();
	}
}

package com.weflop.REST;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.weflop.Game.GameFactory;

@Controller
public class RESTController {

	@PostMapping("/create-game")
	@ResponseBody
	public String createGame(@RequestParam(name="user_id", required=true) String userId,
			@RequestParam(name="small_blind", required=true, defaultValue="0.0") float smallBlind,
			@RequestParam(name="game_type", required=false, defaultValue="0") int gameType) {
		return GameFactory.generateStandardPokerGame(userId, smallBlind); // returning id of newly created game
	}
}

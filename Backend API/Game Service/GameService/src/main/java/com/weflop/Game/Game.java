package com.weflop.Game;

import com.weflop.GameService.REST.GameMetadata;

public interface Game {
	public String getGameId(); // gets game ID

	public void performAction(Action action) throws Exception; // performs an action as a given player

	public GameMetadata getGameMetadata(); // gets game metadata that is exposed through REST controller
}

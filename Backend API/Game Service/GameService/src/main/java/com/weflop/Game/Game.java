package com.weflop.Game;

public interface Game {
	public String getGameId(); // gets game ID

	public void performAction(Action action) throws Exception; // performs an action as a given player

	public GameCustomMetadata getGameMetadata(); // gets metadata custom to game
}

package com.weflop.Game;

public interface Game {
	public String getGameId(); // gets game ID
	public void performAction(Action action) throws Exception; // performs an action as a given player
	public void sendGamePackets() throws Exception; // sends game packets
	public void flushToDatabase(); // flushes game state to database
}

package com.weflop.Game;

public interface Game {
	public void start() throws Exception; // starts a game
	public void end() throws Exception; // cleanly ends a game AND flushes to database
	public void performAction(long playerID, Action action) throws Exception; // performs an action as a given player
	public void sendGamePackets() throws Exception; // sends game packets (i.e. copies of game state) to each player
	public void flushToDatabase() throws Exception; // flushes game state to database
}

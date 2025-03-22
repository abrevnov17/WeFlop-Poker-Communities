package com.weflop.GameService.Game;

import com.weflop.GameService.REST.GameMetadata;

public interface Game {
	public String getGameId(); // gets game ID

	public void performAction(Action action) throws Exception; // performs an action as a given player

	public GameMetadata getGameMetadata(); // gets game metadata that is exposed through REST controller
	
	public boolean archive(String userId);
	
	public boolean canBeRemovedFromReplica(); // true if we can delete game from replica
	
	public void removeFromReplica(); // removes game from replica

	public void unsubscribePlayer(String userId); // game will no longer show up in player games list
}
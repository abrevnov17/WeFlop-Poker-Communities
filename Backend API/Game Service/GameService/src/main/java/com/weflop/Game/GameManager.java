package com.weflop.Game;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GameManager {
	
	/**
	 * Globally accessible map from game id's to game instances.
	 */
	public static Map<String, Game> ID_TO_GAME = new ConcurrentHashMap<String, Game>();
	
	private static ScheduledExecutorService threadExecutor = Executors.newSingleThreadScheduledExecutor();

	private static Map<String, Boolean> idToReadyForCollection = new HashMap<String, Boolean>();
	
	/**
	 * 
	 */
	public static void spawnGarbageCollectorThread() {
		Runnable stateSaver = new Runnable() {
			@Override
			public void run() {
				garbageCollectGames();
			}
		};

		// resent packets after the duration of the turn has passed
		threadExecutor.scheduleAtFixedRate(stateSaver, 3, 3, TimeUnit.MINUTES);
	}
	
	private static void garbageCollectGames() {
		for (Game game : ID_TO_GAME.values()) {
			if (game.canBeRemovedFromReplica() && idToReadyForCollection.get(game.getGameId())) {
				game.removeFromReplica();
			}
			else if (game.canBeRemovedFromReplica()) {
				idToReadyForCollection.put(game.getGameId(), true);
			}
		}
	}
	
}

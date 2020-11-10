package com.weflop.GameService.Game;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
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

	private static Set<String> gamesReadyForCollection = new HashSet<String>();
	
	/**
	 * Spawns a thread that performs garbage collection on inactive games (i.e. removes games with no players
	 * from replica...these games will need to be loaded from db on next attempt).
	 */
	public static void spawnGarbageCollectorThread() {
		Runnable garbageCollector = new Runnable() {
			@Override
			public void run() {
				garbageCollectGames();
			}
		};

		System.out.println("Spawning garbage collection thread...");

		// resent packets after the duration of the turn has passed
		// threadExecutor.scheduleAtFixedRate(garbageCollector, 3, 3, TimeUnit.MINUTES);
	}
	
	/**
	 * Performs game garbage collection (called by parent thread and should be executed on timer).
	 */
	private static void garbageCollectGames() {
		System.out.println("Garbage collection called...");
		System.out.println(ID_TO_GAME.values());
		for (Game game : ID_TO_GAME.values()) {
			if (game.canBeRemovedFromReplica() && gamesReadyForCollection.contains(game.getGameId())) {
				System.out.printf("Removing game (id: %s) from replica...\n", game.getGameId());
				game.removeFromReplica();
				gamesReadyForCollection.remove(game.getGameId());
			} else if (game.canBeRemovedFromReplica()) {
				System.out.printf("Adding game (id: %s) to collection queue...\n", game.getGameId());
				gamesReadyForCollection.add(game.getGameId());
			} else {
				gamesReadyForCollection.remove(game.getGameId());
			}
		}
	}
	
}

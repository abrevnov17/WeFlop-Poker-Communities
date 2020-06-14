package com.weflop.Game;

import java.util.ArrayList;
import java.util.List;

/**
 * A Group is everyone that is either spectating or participating in
 * an individual game.
 * 
 * @author abrevnov
 *
 */
public class Group {
	private Player[] players;
	private List<Player> spectators;
	
	Group(int numPlayers) {
		this.setPlayerSlots(new Player[numPlayers]);
		this.setSpectators(new ArrayList<Player>());
	}
	
	/**
	 * Transitions a player to a spectator.
	 * 
	 * @param player
	 */
	synchronized public void movePlayerToSpectator(Player player) {
		for (int i=0; i < players.length; i++) {
			if (players[i].equals(player)) {
				players[i] = null; // removing player from players
				player.convertToSpectator(); // converting player to spectator
				spectators.add(player); // adding player to spectators list
			}
		}
	}
	
	
	/* Getters and Setters */

	synchronized public List<Player> getPlayers() {
		List<Player> nonNullPlayers = new ArrayList<Player>();
		for (Player player : this.players) {
			if (player != null) {
				nonNullPlayers.add(player);
			}
		}
		return nonNullPlayers;
	}
	
	/**
	 * Gets index of player when considering a list our non-null players.
	 * 
	 * @return Index of player in list
	 */
	synchronized public int getIndexOfPlayerInList(Player player) {
		return this.getPlayers().indexOf(player);
	}

	synchronized public void getPlayerSlots(Player[] players) {
		this.players = players;
	}
	
	synchronized public void setPlayerSlots(Player[] players) {
		this.players = players;
	}

	synchronized public List<Player> getSpectators() {
		return spectators;
	}

	synchronized public void setSpectators(List<Player> spectators) {
		this.spectators = spectators;
	}
}

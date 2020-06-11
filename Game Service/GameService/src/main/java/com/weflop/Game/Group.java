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
		this.setPlayers(new Player[numPlayers]);
		this.setSpectators(new ArrayList<Player>());
	}

	public Player[] getPlayers() {
		return players;
	}

	public void setPlayers(Player[] players) {
		this.players = players;
	}

	public List<Player> getSpectators() {
		return spectators;
	}

	public void setSpectators(List<Player> spectators) {
		this.spectators = spectators;
	}
	
	
}

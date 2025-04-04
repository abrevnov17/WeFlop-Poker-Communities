package com.weflop.GameService.Game;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A Pot has a float value (indicating pot size) and a set of player ids
 * who have a (potential) claim to that pot. 
 * 
 * @author abrevnov
 *
 */
public class Pot {
	private float size;
	private Set<Player> players;
	
	public Pot() {
		this.size = 0.00f;
		this.players = new HashSet<Player>();
	}
	
	@Override
	public String toString() {
		List<String> playerIds = this.players.stream().map(player -> player.getId()).collect(Collectors.toList());
		return "Size: " + size + ", Players: " + playerIds.toString();
	}
	
	public void addPlayer(Player player) {
		players.add(player);
	}
	
	public void removePlayer(Player player) {
		players.remove(player);
	}

	public boolean doesPlayerHaveClaim(Player player) {
		return players.contains(player);
	}
	
	public void addAllPlayers(Collection<Player> players) {
		this.players.addAll(players);
	}
	
	/* Getters and Setters */
	
	public float getSize() {
		return size;
	}

	public void setSize(float size) {
		this.size = size;
	}

	public Set<Player> getPlayers() {
		return players;
	}

	public void setPlayers(Set<Player> players) {
		this.players = players;
	}
	
}

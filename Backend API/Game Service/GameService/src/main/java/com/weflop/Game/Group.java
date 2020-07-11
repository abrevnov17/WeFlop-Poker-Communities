package com.weflop.Game;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.Assert;
import org.springframework.web.socket.WebSocketSession;

/**
 * A Group is everyone that is either spectating or participating in an
 * individual game.
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
		Assert.isTrue(player.isSpectating(), "Must be spectator to sit");

		for (int i = 0; i < players.length; i++) {
			if (players[i].equals(player)) {
				players[i] = null; // removing player from players
				player.convertToSpectator(); // converting player to spectator
				spectators.add(player); // adding player to spectators list
			}
		}
	}

	/**
	 * Transitions a spectator to an active player.
	 * 
	 * @param player
	 */
	synchronized public void moveSpectatorToActivePlayer(Player spectator, Integer slot) {
		Assert.isTrue(spectator.isSpectating(), "Must be spectator to sit");
		Assert.isTrue(slot != null, "Slot must be specified in order to sit");
		Assert.isTrue(players[slot] == null, "Seat is already taken");

		spectator.setSlot(slot);

		// switch player state to waiting for round
		spectator.setState(PlayerState.WAITING_FOR_ROUND);

		// remove participant from spectators and move to active players
		spectators.remove(spectator);

		players[slot] = spectator;
	}

	/**
	 * Finds first empty slot in table and returns the index of that slot.
	 * 
	 * @return Index of empty slot or -1 if no such slot exists
	 */
	synchronized public int getFirstEmptySlot() {
		for (int i = 0; i < players.length; i++) {
			if (players[i] == null) {
				return i;
			}
		}

		return -1;
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

	synchronized public void removePlayerFromTable(Player player) {
		for (int i = 0; i < players.length; i++) {
			if (players[i] != null && players[i].equals(player)) {
				players[i] = null;
			}
		}
		player.setSlot(-1);
	}

	synchronized public void setAllPlayersToState(PlayerState state) {
		for (int i = 0; i < players.length; i++) {
			if (players[i] != null) {
				players[i].setState(state);
			}
		}
	}

	synchronized public void deleteParticipant(Player participant) {
		if (participant.isSpectating()) {
			// need to delete from spectators
			this.spectators.remove(participant);
		} else {
			// need to delete from array of active players
			removePlayerFromTable(participant);
		}
	}

	/**
	 * Creates a spectator and adds it to list of spectators.
	 */
	synchronized public void createSpectator(String id, WebSocketSession session) {
		this.spectators.add(new Player(id, session));
	}
}

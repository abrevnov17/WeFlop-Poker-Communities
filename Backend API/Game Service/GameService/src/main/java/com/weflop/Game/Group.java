package com.weflop.Game;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
	
	private int smallBlindIndex; // -1 if no small blind
	private int bigBlindIndex;
	private int dealerIndex;

	Group(int numPlayers) {
		this.setPlayerSlots(new Player[numPlayers]);
		this.setSpectators(new ArrayList<Player>());
		
		this.smallBlindIndex = -1;
		this.bigBlindIndex = -1;
		this.dealerIndex = -1;
	}

	/**
	 * Transitions a player to a spectator.
	 * 
	 * @param player
	 */
	synchronized public void movePlayerToSpectator(Player player) {
		for (int i = 0; i < players.length; i++) {
			if (players[i] != null && players[i].equals(player)) {
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
		Assert.isTrue(slot != null, "Slot must be specified in order to sit");
		Assert.isTrue(players[slot] == null, "Seat is already taken");

		spectator.sit(slot);

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

	synchronized public Player[] getPlayerSlots() {
		return this.players;
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
	
	/**
	 * Sets current state to nextHandState for players. Does NOT change nextHandState.
	 */
	synchronized public void transitionPlayerStates() {
		for (int i = 0; i < players.length; i++) {
			if (players[i] != null) {
				if (players[i].getState() == PlayerState.WAITING_FOR_HAND) {
					players[i].setNextHandState(PlayerState.WAITING_FOR_TURN);
				}
				players[i].transitionState();
			}
		}
	}
	
	/**
	 * Finds which slot player belongs to. Returns -1 if no slot exists
	 * @param player
	 * @return Slot (zero-indexed)
	 */
	synchronized public int getPlayerSlot(Player player) {
		for (int i = 0; i < players.length; i++) {
			if (players[i] != null && players[i].equals(player)) {
				return i;
			}
		}
		
		return -1;
	}
	
	/**
	 * Returns list of all players that could pay a blind.
	 */
	synchronized public List<Player> getPlayersEligibleForBlind() {
		return getPlayers().stream().filter(player -> player.canBeBlind()).collect(Collectors.toList());
	}
	
	/**
	 * Returns list of players that elected to post big blind (but may not necessarily have paid yet).
	 */
	synchronized public List<Player> getPlayersWhoHavePostedBigBlind() {
		return getPlayers().stream().filter(player -> player.getPrevState() == PlayerState.POSTING_BIG_BLIND).collect(Collectors.toList());
	}
	
	
	/**
	 * Returns our list of (non-null) players starting at the player after the inputted slot.
	 * @param slot
	 * @return List of players.
	 */
	synchronized public List<Player> getPlayersClockwiseAfterSlot(int slot) {
		List<Player> playersBeginningWithSlot = new ArrayList<Player>();
		
		for (int index = (slot+1) % players.length; index != slot; index = (index + 1) % players.length) {
			if (players[index] != null)
				playersBeginningWithSlot.add(players[index]);
		}
		
		if (players[slot] != null)
			playersBeginningWithSlot.add(players[slot]);
		
		return playersBeginningWithSlot;
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
	
	synchronized public List<Player> getAllParticipants() {
		List<Player> participants = new ArrayList<Player>(this.getPlayers());
		participants.addAll(this.getSpectators());
		return participants;
	}
	
	synchronized public boolean allWaitingPlayersInCheckedState() {
		for (Player player : getPlayers()) {
			if (player.canMoveInRound() && player.getState() != PlayerState.CHECKED) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Resets all player round-bets to zero (called at ends of rounds).
	 */
	synchronized public void resetPlayerRoundBets() {
		for (int i = 0; i < players.length; i++) {
			if (players[i] != null) {
				players[i].setCurrentRoundBet(0);
			}
		}
	}
	
	/**
	 * Resets player states where appropriate (such as flipping from CHECKED to WAITING_FOR_TURN).
	 */
	synchronized public void preparePlayerStatesForNewRound() {
		for (int i = 0; i < players.length; i++) {
			if (players[i] != null && players[i].getState() == PlayerState.CHECKED) {
				players[i].setState(PlayerState.WAITING_FOR_TURN);
			}
		}
	}

	/**
	 * Creates a spectator and adds it to list of spectators.
	 */
	synchronized public void createSpectator(String id, WebSocketSession session) {
		this.spectators.add(new Player(id, session));
	}
	
	/**
	 * Gets all active players for the given betting round
	 * (i.e. players who are not spectating, waiting for next round, or have folded).
	 * @return List of active players.
	 */
	synchronized public List<Player> getActivePlayersInBettingRound() {
		return getPlayers().stream().filter(player -> player.isActiveInBettingRound()).collect(Collectors.toList());
	}
	
	/**
	 * Gets all active players for the given hand
	 * (i.e. players who were or are involved in the current hand).
	 * @return List of active players.
	 */
	synchronized public List<Player> getActivePlayersInHand() {
		return getPlayers().stream().filter(player -> player.isActive()).collect(Collectors.toList());
	}
	
	/**
	 * Called at end of hand. Updates dealerIndex, smallBlindIndex, and bigBlindIndex.
	 */
	synchronized public void cycleDealer() {
		// resetting bigBlind and smallBlind to -1 (not set)
		this.smallBlindIndex = -1;
		this.bigBlindIndex = -1;
		
		for (int i=0; i < players.length; i++) {
			this.dealerIndex = (this.dealerIndex + i) % players.length;
			
			if (players[dealerIndex] != null && players[dealerIndex].isActive()) {
				break;
			}
		}
		
		for (int i=0; i < players.length; i++) {
			this.smallBlindIndex = (this.dealerIndex + i) % players.length;
			
			if (players[smallBlindIndex] != null && players[smallBlindIndex].canBeBlind()) {
				if (players[smallBlindIndex].getPrevState() == PlayerState.POSTING_BIG_BLIND) {
					// player should steal dealer index
					this.dealerIndex = smallBlindIndex;
					continue;
				} else if (players[smallBlindIndex].isWaitingForBigBlind()) {
					// big blind somehow "jumped" over this player
					// player should become big blind, and no small blind should be set
					this.bigBlindIndex = smallBlindIndex;
					this.smallBlindIndex = -1;
					
					// player is waiting for turn as they are now big blind
					players[bigBlindIndex].updateCurrentAndFutureState(PlayerState.WAITING_FOR_TURN, PlayerState.WAITING_FOR_TURN);
					
					return;
				}
				
				break;
			} else if (players[smallBlindIndex] != null && players[smallBlindIndex].getState() == PlayerState.WAITING_FOR_HAND) {
				players[smallBlindIndex].updateCurrentAndFutureState(PlayerState.WAITING_FOR_BIG_BLIND, PlayerState.WAITING_FOR_BIG_BLIND);
			}
		}
		
		for (int i=0; i < players.length; i++) {
			this.bigBlindIndex = (this.smallBlindIndex + i) % players.length;
			
			if (players[bigBlindIndex] != null && players[bigBlindIndex].canBeBlind()) {
				players[bigBlindIndex].updateCurrentAndFutureState(PlayerState.WAITING_FOR_TURN, PlayerState.WAITING_FOR_TURN);
				break;
			} else if (players[bigBlindIndex] != null && players[smallBlindIndex].getState() == PlayerState.WAITING_FOR_HAND) {
				players[smallBlindIndex].updateCurrentAndFutureState(PlayerState.WAITING_FOR_BIG_BLIND, PlayerState.WAITING_FOR_BIG_BLIND);
			}
		}
	}
	
	public Player getSmallBlindPlayer() {
		if (smallBlindIndex == -1) {
			return null;
		}
		return players[smallBlindIndex];
	}
	
	public Player getBigBlindPlayer() {
		if (bigBlindIndex == -1) {
			return null;
		}
		return players[bigBlindIndex];
	}

	public int getSmallBlindIndex() {
		return smallBlindIndex;
	}

	public void setSmallBlindIndex(int smallBlindIndex) {
		this.smallBlindIndex = smallBlindIndex;
	}

	public int getBigBlindIndex() {
		return bigBlindIndex;
	}

	public void setBigBlindIndex(int bigBlindIndex) {
		this.bigBlindIndex = bigBlindIndex;
	}
}

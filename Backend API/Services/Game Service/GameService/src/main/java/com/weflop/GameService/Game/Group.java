package com.weflop.GameService.Game;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.util.Assert;
import org.springframework.web.socket.WebSocketSession;

import com.google.common.collect.Sets;
import com.weflop.GameService.Database.DomainObjects.GroupPOJO;
import com.weflop.GameService.Database.DomainObjects.PlayerPOJO;
import com.weflop.GameService.Database.DomainObjects.SpectatorPOJO;

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

	private Set<Player> playersWhoCanMuck; // list of players with option to muck

	Group(int numPlayers) {
		this.setPlayerSlots(new Player[numPlayers]);
		this.setSpectators(new ArrayList<Player>());

		this.smallBlindIndex = -1;
		this.bigBlindIndex = -1;
		this.dealerIndex = -1;

		this.setPlayersWhoCanMuck(Sets.newConcurrentHashSet());
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

	/* POJO conversions */

	public static List<Player> playersFromPOJO(List<PlayerPOJO> players) {
		return players.stream().map(pojo -> Player.fromPOJO(pojo)).collect(Collectors.toList());
	}

	public static List<Player> spectatorsFromPOJO(List<SpectatorPOJO> spectators) {
		return spectators.stream().map(pojo -> Player.fromSpectatorPOJO(pojo)).collect(Collectors.toList());
	}

	public static Group fromPOJO(int numPlayers, GroupPOJO pojo) {
		Group group = new Group(numPlayers);

		group.dealerIndex = pojo.getDealerIndex();
		group.smallBlindIndex = pojo.getSmallBlindIndex();
		group.bigBlindIndex = pojo.getBigBlindIndex();

		group.spectators = Group.spectatorsFromPOJO(pojo.getSpectators());

		for (Player player : Group.playersFromPOJO(pojo.getPlayers())) {
			group.players[player.getSlot()] = player;
		}

		return group;
	}

	public List<PlayerPOJO> playersToPOJO() {
		return getPlayers().stream().map(player -> player.toPOJO()).collect(Collectors.toList());
	}

	public List<SpectatorPOJO> spectatorsToPOJO() {
		return getSpectators().stream().map(spectator -> spectator.toSpectatorPOJO()).collect(Collectors.toList());
	}

	public GroupPOJO toPOJO() {
		return new GroupPOJO(this.playersToPOJO(), this.spectatorsToPOJO(), this.smallBlindIndex, this.bigBlindIndex,
				this.dealerIndex);
	}

	/* Getters and Setters */

	/**
	 * Attempts to get participant by id. Returns null if no participant exists with
	 * provided id.
	 * 
	 * @param id
	 * @return Participant with id
	 */
	public Player getParticipantById(String id) {
		for (Player participant : this.getAllParticipants()) {
			if (participant.getId().equals(id)) {
				return participant;
			}
		}

		return null;
	}

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

	synchronized public void setAllPlayersCurrentAndFutureStates(PlayerState currentState, PlayerState nextState) {
		for (int i = 0; i < players.length; i++) {
			if (players[i] != null) {
				players[i].updateCurrentAndFutureState(currentState, nextState);
			}
		}
	}

	/**
	 * Sets current state to nextHandState for players. Does NOT change
	 * nextHandState.
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
	 * 
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
	 * Returns list of players that elected to post big blind (but may not
	 * necessarily have paid yet).
	 */
	synchronized public List<Player> getPlayersWhoHavePostedBigBlind() {
		return getPlayers().stream().filter(player -> player.getPrevState() == PlayerState.POSTING_BIG_BLIND)
				.collect(Collectors.toList());
	}

	/**
	 * Returns our list of (non-null) players starting at the player at the inputted
	 * slot.
	 * 
	 * @param slot
	 * @return List of players.
	 */
	synchronized public List<Player> getPlayersClockwiseFromSlot(int slot) {
		List<Player> playersBeginningWithSlot = new ArrayList<Player>();

		int index;
		for (index = slot % players.length; index != (((slot - 1) % players.length) + players.length)
				% players.length; index = (index + 1) % players.length) {
			if (players[index] != null)
				playersBeginningWithSlot.add(players[index]);
		}

		if (players[index] != null)
			playersBeginningWithSlot.add(players[index]);

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
	 * Resets all player round-bets to zero (called at ends of individual betting
	 * rounds).
	 */
	synchronized public void resetPlayerRoundBets() {
		for (int i = 0; i < players.length; i++) {
			if (players[i] != null) {
				players[i].setRoundBet(0.00f);
			}
		}
	}

	/**
	 * Resets bets players made during overall hand.
	 */
	synchronized public void resetHandBets() {
		for (int i = 0; i < players.length; i++) {
			if (players[i] != null) {
				players[i].setHandBet(0.00f);
			}
		}
	}

	/**
	 * Resets player states where appropriate (such as flipping from CHECKED to
	 * WAITING_FOR_TURN).
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
	 * Gets all active players for the given betting round (i.e. players who are not
	 * spectating, waiting for next round, or have folded).
	 * 
	 * @return List of active players.
	 */
	synchronized public List<Player> getActivePlayersInBettingRound() {
		return getPlayers().stream().filter(player -> player.isActiveInBettingRound()).collect(Collectors.toList());
	}

	/**
	 * Gets all active players for the given hand (i.e. players who were or are
	 * involved in the current hand).
	 * 
	 * @return List of active players.
	 */
	synchronized public List<Player> getActivePlayersInHand() {
		return getPlayers().stream().filter(player -> player.isActive()).collect(Collectors.toList());
	}

	synchronized public List<Player> getPlayersPlaying() {
		return getPlayers().stream().filter(player -> player.isPlaying()).collect(Collectors.toList());
	}
	
	synchronized public List<Player> getFoldedPlayers(){
		return getPlayers().stream().filter(player -> player.hasFolded()).collect(Collectors.toList());
	}
	

	/**
	 * Prepares state for next hand. Called at end of hand.
	 */
	public void resetForNewHand() {

		this.cycleDealer(); // cycling dealer, small blind, big blind
		this.playersWhoCanMuck.clear();

		// setting hand balances to be player total balance
		for (Player player : this.getPlayers()) {
			player.setHandBalance(player.getBalance());
			player.setHandBet(0.00f);
			player.setRoundBet(0.00f);
		}
	}

	synchronized public void cycleDealer() {
		System.out.println("CYCLING DEALER");
		// Reindex blinds if game is heads-up
		int oldsb = this.smallBlindIndex;
		for (int i = 1; i < players.length + 1; i++) {
			this.bigBlindIndex = (((this.bigBlindIndex + i) % players.length) + players.length) % players.length;
			if ((players[this.bigBlindIndex] != null) && ((players[this.bigBlindIndex].getState() == PlayerState.BUSTED)|| (players[this.bigBlindIndex].getMissedBlind() && (players[this.bigBlindIndex].getState() == PlayerState.SITTING_OUT)))) {
				if (players[this.bigBlindIndex].isDisplayingInactivity()) {
					players[this.bigBlindIndex].convertToSpectator();
				} else {
					players[this.bigBlindIndex].setDisplayingInactivity(true);
				}
			}else if ((players[this.bigBlindIndex] != null) && (!players[this.bigBlindIndex].getMissedBlind() && (players[this.bigBlindIndex].getState() == PlayerState.SITTING_OUT))){
				players[this.bigBlindIndex].setMissedBlind(true);
				System.out.println("\n\n\n\n\n\nSETTTING MISSED BLINDS\n\n\n\n\n\n\n\n");
			}else if ((players[this.bigBlindIndex] != null) && players[this.bigBlindIndex].canBeBlind()) {
				players[this.bigBlindIndex].updateCurrentAndFutureState(PlayerState.WAITING_FOR_TURN,
						PlayerState.WAITING_FOR_TURN);
				break;
			} else if ((players[this.bigBlindIndex] != null)
					&& players[this.bigBlindIndex].getState() == PlayerState.WAITING_FOR_HAND) {
				players[this.bigBlindIndex].updateCurrentAndFutureState(PlayerState.WAITING_FOR_BIG_BLIND,
						PlayerState.WAITING_FOR_BIG_BLIND);
			}
		}
		System.out.printf("\n0BB: %d SB: %d D: %d\n",this.bigBlindIndex,this.smallBlindIndex,this.dealerIndex);

		for (int i = -1; i > -(players.length + 1); i--) {
			this.smallBlindIndex = ((((this.bigBlindIndex + i) % players.length) + players.length) % players.length);
			if ((players[this.smallBlindIndex] != null) && (players[this.smallBlindIndex].isActive())) {
				break;
			}
		}
		System.out.printf("\n1BB: %d SB: %d D: %d\n",this.bigBlindIndex,this.smallBlindIndex,this.dealerIndex);

		int currentPlayers;
		currentPlayers = getPlayersPlaying().size();
		if (currentPlayers == 2) {
			this.dealerIndex = this.smallBlindIndex;
		} else {
			for (int i = -1; i > -(players.length + 1); i--) {
				this.dealerIndex = ((((this.smallBlindIndex + i) % players.length) + players.length) % players.length);

				if ((players[this.dealerIndex] != null) && (players[this.dealerIndex].isActive())) {
					break;
				}
			}
		}
		if (oldsb == this.smallBlindIndex) {
			this.smallBlindIndex = -1;
		}
		System.out.printf("\n2BB: %d SB: %d D: %d\n",this.bigBlindIndex,this.smallBlindIndex,this.dealerIndex);
//		for (int i = 1; i < players.length + 1; i++) {
//			int startPlayerIndex = ((((this.bigBlindIndex + i) % players.length) + players.length) % players.length);
//			
//		
		
		
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

	public Player getDealerPlayer() {
		if (dealerIndex == -1) {
			return null;
		}
		return players[dealerIndex];
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

	public int getDealerIndex() {
		return dealerIndex;
	}

	public void setDealerIndex(int dealerIndex) {
		this.dealerIndex = dealerIndex;
	}

	public Set<Player> getPlayersWhoCanMuck() {
		return playersWhoCanMuck;
	}

	public void setPlayersWhoCanMuck(Set<Player> playersWhoCanMuck) {
		this.playersWhoCanMuck = playersWhoCanMuck;
	}
}

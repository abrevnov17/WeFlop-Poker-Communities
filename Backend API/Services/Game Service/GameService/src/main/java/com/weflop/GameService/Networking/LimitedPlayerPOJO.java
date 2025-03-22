package com.weflop.GameService.Networking;

import java.util.List;
import java.util.stream.Collectors;

import com.weflop.GameService.Database.DomainObjects.PlayerPOJO;
import com.weflop.GameService.Game.Player;

/**
 * CRUD object wrapping player information for another player (that is not the
 * current user). When updating game states, players need to be updated on
 * players current bets as well as their respective states. However, we want to
 * omit information about player hands and balances from these players.
 * 
 * @author abrevnov
 *
 */
public class LimitedPlayerPOJO {

	private String id;

	private float handBalance;

	private float roundBet;
	
	private String state;
	
	private int slot;
	
	private float balance;

	public LimitedPlayerPOJO(String id, float handBalance, float roundBet, String state, int slot, float balance) {
		this.id = id;
		this.handBalance = handBalance;
		this.roundBet = roundBet;
		this.state = state;
		this.setSlot(slot);
		this.setBalance(balance);
	}

	/**
	 * Converts a PlayerPOJO instance to a LimitedPlayerPOJO instance.
	 * 
	 * @param player
	 * @return Corresponding instance of LimitedPlayerPOJO
	 */
	public static LimitedPlayerPOJO fromPlayerPOJO(PlayerPOJO player) {
		return new LimitedPlayerPOJO(player.getId(), player.getHandBalance(), 
				player.getRoundBet(), player.getState(), player.getSlot(), player.getBalance());
	}
	
	public static List<LimitedPlayerPOJO> fromPlayers(List<Player> players) {
		return players.stream().map(p -> LimitedPlayerPOJO.fromPlayerPOJO(p.toPOJO())).collect(Collectors.toList());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public float getRoundBet() {
		return roundBet;
	}

	public void setRoundBet(float roundBet) {
		this.roundBet = roundBet;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public float getHandBalance() {
		return handBalance;
	}

	public void setHandBalance(float handBalance) {
		this.handBalance = handBalance;
	}

	public int getSlot() {
		return slot;
	}

	public void setSlot(int slot) {
		this.slot = slot;
	}

	public float getBalance() {
		return balance;
	}

	public void setBalance(float balance) {
		this.balance = balance;
	}
}

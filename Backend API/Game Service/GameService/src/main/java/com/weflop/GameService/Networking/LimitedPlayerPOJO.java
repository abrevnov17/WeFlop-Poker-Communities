package com.weflop.GameService.Networking;

import com.weflop.GameService.Database.DomainObjects.PlayerPOJO;

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

	private float currentBet;

	private String state;

	public LimitedPlayerPOJO(String id, float currentBet, String state) {
		this.id = id;
		this.currentBet = currentBet;
		this.state = state;
	}

	/**
	 * Converts a PlayerPOJO instance to a LimitedPlayerPOJO instance.
	 * 
	 * @param player
	 * @return Corresponding instance of LimitedPlayerPOJO
	 */
	public static LimitedPlayerPOJO fromPlayerPOJO(PlayerPOJO player) {
		return new LimitedPlayerPOJO(player.getId(), player.getCurrentBet(), player.getState());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public float getCurrentBet() {
		return currentBet;
	}

	public void setCurrentBet(float currentBet) {
		this.currentBet = currentBet;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
}

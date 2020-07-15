package com.weflop.Game;

import java.util.List;
import java.util.stream.Collectors;

import com.weflop.GameService.Database.DomainObjects.InitialStatePOJO;
import com.weflop.GameService.Database.DomainObjects.PlayerPOJO;

/**
 * Describes initial state of game.
 * 
 * @author abrevnov
 *
 */
public class InitialState {
	private List<Player> initPlayers;

	public InitialState(List<Player> initPlayers) {
		this.initPlayers = initPlayers;
	}

	/**
	 * Converts to POJO representation.
	 * 
	 * @return Corresponding instance of InitialStatePOJO
	 */
	public InitialStatePOJO toPOJO() {
		List<PlayerPOJO> players = initPlayers.stream().map(player -> player.toPlayerPOJO())
				.collect(Collectors.toList());

		return new InitialStatePOJO(players);
	}

	/* Getters and Setters */

	public List<Player> getInitPlayers() {
		return initPlayers;
	}

	public void setInitPlayers(List<Player> initPlayers) {
		this.initPlayers = initPlayers;
	}
}

package com.weflop.Database.DomainObjects;

import java.util.List;

/**
 * CRUD object wrapper for initial state of game.
 * 
 * @author abrevnov
 *
 */
public class InitialStatePOJO {
	private List<PlayerPOJO> initPlayers;
	
	public InitialStatePOJO(List<PlayerPOJO> initPlayers) {
		this.initPlayers = initPlayers;
	}

	public List<PlayerPOJO> getInitPlayers() {
		return initPlayers;
	}

	public void setInitPlayers(List<PlayerPOJO> initPlayers) {
		this.initPlayers = initPlayers;
	}
}

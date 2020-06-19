package com.weflop.Database.DomainObjects;

import java.util.List;

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

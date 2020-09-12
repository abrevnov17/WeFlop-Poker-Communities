package com.weflop.GameService.Database.DomainObjects;

public class TurnPOJO {
	
	private PlayerPOJO player;
	
	private long startTime; // value of system timer in nanoseconds at start

	public TurnPOJO(PlayerPOJO player, long startTime) {
		this.setPlayer(player);
		this.setStartTime(startTime);
	}

	public PlayerPOJO getPlayer() {
		return player;
	}

	public void setPlayer(PlayerPOJO player) {
		this.player = player;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
}

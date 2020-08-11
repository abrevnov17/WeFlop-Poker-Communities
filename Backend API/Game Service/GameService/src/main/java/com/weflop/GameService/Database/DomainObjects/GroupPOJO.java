package com.weflop.GameService.Database.DomainObjects;

import java.util.List;

public class GroupPOJO {
	
	private List<PlayerPOJO> players;
	
	private List<SpectatorPOJO> spectators;
	
	private int smallBlindIndex;
	
	private int bigBlindIndex;
	
	private int dealerIndex;

	public GroupPOJO(List<PlayerPOJO> players, List<SpectatorPOJO> spectators, int smallBlindIndex, int bigBlindIndex,
			int dealerIndex) {
		super();
		this.players = players;
		this.spectators = spectators;
		this.smallBlindIndex = smallBlindIndex;
		this.bigBlindIndex = bigBlindIndex;
		this.dealerIndex = dealerIndex;
	}

	public List<PlayerPOJO> getPlayers() {
		return players;
	}

	public void setPlayers(List<PlayerPOJO> players) {
		this.players = players;
	}

	public List<SpectatorPOJO> getSpectators() {
		return spectators;
	}

	public void setSpectators(List<SpectatorPOJO> spectators) {
		this.spectators = spectators;
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
}

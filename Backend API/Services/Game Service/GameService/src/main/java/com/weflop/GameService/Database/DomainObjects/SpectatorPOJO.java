package com.weflop.GameService.Database.DomainObjects;

/**
 * CRUD object wraps information pertaining to spectators
 * 
 * @author abrevnov
 *
 */
public class SpectatorPOJO {
	private String userId;

	public SpectatorPOJO(String userId) {
		this.userId = userId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
}

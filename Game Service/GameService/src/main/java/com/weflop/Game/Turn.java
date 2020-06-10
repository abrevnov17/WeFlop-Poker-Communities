package com.weflop.Game;

import java.time.Duration;

/**
 * A Turn is a wrapper-object that contains information regarding the
 * current term inside of a game.
 * 
 * @author abrevnov
 *
 */
public class Turn {
	private Player player;
	private Duration timeElapsed;
	
	Turn(Player player, Duration timeElapsed) {
		this.setPlayer(player);
		this.setTimeElapsed(timeElapsed);
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public Duration getTimeElapsed() {
		return timeElapsed;
	}

	public void setTimeElapsed(Duration timeElapsed) {
		this.timeElapsed = timeElapsed;
	}
}

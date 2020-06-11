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
	
	public Turn(Player player, Duration timeElapsed) {
		this.setPlayer(player);
		this.setTimeElapsed(timeElapsed);
	}

	synchronized public Player getPlayer() {
		return player;
	}

	synchronized public void setPlayer(Player player) {
		this.player = player;
	}

	synchronized public Duration getTimeElapsed() {
		return timeElapsed;
	}

	synchronized public void setTimeElapsed(Duration timeElapsed) {
		this.timeElapsed = timeElapsed;
	}
}

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
	
	private int count; // number of turns since game start
	private Player player;
	private long startTime; // value of system timer in nanoseconds at start
	
	
	public Turn(Player player, long startTime) {
		this.setPlayer(player);
		this.setStartTime(startTime);
		this.setCount(0);
	}
	
	public Turn(Player player, long startTime, int count) {
		this.setPlayer(player);
		this.setStartTime(startTime);
		this.setCount(count);
	}

	synchronized public Player getPlayer() {
		return player;
	}

	synchronized public void setPlayer(Player player) {
		this.player = player;
	}

	synchronized public Duration getTimeElapsed(long currentTime) {
		return Duration.ofNanos(currentTime - startTime);
	}

	synchronized public void fixTimeElapsed(long currentTime, Duration timeElapsed) {
		this.startTime = currentTime - timeElapsed.toNanos();
	}
	
	synchronized public long getStartTime() {
		return startTime;
	}

	synchronized public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	synchronized public int getCount() {
		return count;
	}

	synchronized public void setCount(int count) {
		this.count = count;
	}
}

package com.weflop.Game;

import java.time.Duration;

/**
 * A Turn is a wrapper-object that contains information regarding the current
 * term inside of a game.
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
	
	/**
	 * Takes in a player instance and increments the turn and sets the given
	 * player to be the player associated with the turn. Updates turn time
	 * @param player
	 */
	synchronized public void nextTurn(Player player) {
		this.setPlayer(player);
		this.setStartTime(System.nanoTime());
		this.setCount(this.getCount() + 1);
		this.getPlayer().setState(PlayerState.CURRENT_TURN);
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

	synchronized public Duration getTimeRemaining(long currentTime, Duration turnDuration) {
		return turnDuration.minus(Duration.ofNanos(currentTime - startTime));
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

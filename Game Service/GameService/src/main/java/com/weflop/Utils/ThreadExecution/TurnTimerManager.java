package com.weflop.Utils.ThreadExecution;

import com.weflop.Game.AbstractGame;

/**
 * Provides a method that handles when a turn timer expires.
 * 
 * @author abrevnov
 *
 */
public class TurnTimerManager implements Runnable {
	
	private AbstractGame game;
	private int currentTurn;

    public TurnTimerManager(AbstractGame game, int currentTurn) {
        this.game = game;
        this.currentTurn = currentTurn;
    }
	
	@Override
	public void run() {
		// when a timer has expired, we first check to see if the game turn has already advanced
		this.game.turnExpired(currentTurn);
	}

}

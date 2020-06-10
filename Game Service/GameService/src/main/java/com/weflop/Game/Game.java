package com.weflop.Game;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.weflop.Cards.Card;

/**
 * Game.java
 * 
 * Abstract class defining some shared properties and methods as well
 * as some required abstract methods for all instances of games.
 * 
 * @author abrevnov
 *
 */
public abstract class Game {
	protected UUID id;
	
	protected float pot;
	protected List<Card> centerCards;
	protected Duration timeElapsed;
	protected int dealerIndex;
	
	protected float smallBlind;
	protected float bigBlind; // almost always, just 2x the smallBlind
	
	protected List<Player> players;
	protected Turn turn;
	
	protected Game(float smallBlind) {
		this.id = UUID.randomUUID();
		this.pot = 0.0f;
		this.centerCards = new ArrayList<Card>();
		this.dealerIndex = 0;
		this.timeElapsed = Duration.ZERO;
		this.smallBlind = smallBlind;
		this.bigBlind = 2.0f*smallBlind;
		this.players = new ArrayList<Player>();
		this.turn = null; // is not initialized until game starts
	}
	
	// if the big blind is not just 2x small blind
	protected Game(float smallBlind, float bigBlind) {
		this(smallBlind);
		this.bigBlind = bigBlind;
	}
	
	public abstract void start() throws Exception; // starts a game
	public abstract void end() throws Exception; // cleanly ends a game AND flushes to database
	public abstract void performAction(long playerID, Action action) throws Exception; // performs an action as a given player
	public abstract void sendPackets() throws Exception; // sends game packets (i.e. copies of game state) to each player
	public abstract void flushToDatabase() throws Exception; // flushes game state to database
}

package com.weflop.Game;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.weflop.Cards.Card;

/**
 * 
 * Abstract class defining some shared properties and methods as well
 * as some required abstract methods for all instances of games.
 * 
 * @author abrevnov
 *
 */
public abstract class AbstractGame {
	
	private final UUID id;
	
	private Lock lock; // manages concurrent read/write access to game properties

	private boolean started;
	private Instant startTime;
	
	private float pot;

	private List<Card> centerCards;
	private int dealerIndex;
	
	private float smallBlind;
	private float bigBlind; // almost always just 2x the smallBlind
	
	private Group group; // our group of players

	private Turn turn;
		
	protected AbstractGame(float smallBlind, int tableSize) {
		this.id = UUID.randomUUID();
		this.setPot(0.0f);
		this.centerCards = new ArrayList<Card>();
		this.dealerIndex = 0;
		this.setStartTime(null); // do not start clock till start() called
		this.smallBlind = smallBlind;
		this.bigBlind = 2.0f*smallBlind;
		this.setGroup(new Group(tableSize));
		this.turn = null; // is not initialized until game starts
		this.setStarted(false);
		this.setLock(new ReentrantLock());
	}
	
	// if the big blind is not just 2x small blind
	protected AbstractGame(float smallBlind, float bigBlind, int tableSize) {
		this(smallBlind, tableSize);
		this.bigBlind = bigBlind;
	}
	
	/* These methods should be overriden by subclasses: */
	
	public abstract void start() throws Exception; // starts a game
	public abstract void end() throws Exception; // cleanly ends a game AND flushes to database
	public abstract void performAction(long playerID, Action action) throws Exception; // performs an action as a given player
	public abstract void sendPackets() throws Exception; // sends game packets (i.e. copies of game state) to each player
	public abstract void flushToDatabase() throws Exception; // flushes game state to database

	/* Getters and setters for universal game properties */
	protected UUID getId() {
		return id;
	}

	synchronized protected boolean isStarted() {
		return started;
	}

	synchronized protected void setStarted(boolean started) {
		this.started = started;
	}

	synchronized protected Instant getStartTime() {
		return startTime;
	}

	synchronized protected void setStartTime(Instant startTime) {
		this.startTime = startTime;
	}

	synchronized protected float getPot() {
		return pot;
	}

	synchronized protected void setPot(float pot) {
		this.pot = pot;
	}
	
	synchronized protected List<Card> getCenterCards() {
		return centerCards;
	}

	synchronized protected void setCenterCards(List<Card> centerCards) {
		this.centerCards = centerCards;
	}

	synchronized protected int getDealerIndex() {
		return dealerIndex;
	}

	synchronized protected void setDealerIndex(int dealerIndex) {
		this.dealerIndex = dealerIndex;
	}

	synchronized protected float getSmallBlind() {
		return smallBlind;
	}

	synchronized protected void setSmallBlind(float smallBlind) {
		this.smallBlind = smallBlind;
	}

	synchronized protected float getBigBlind() {
		return bigBlind;
	}

	synchronized protected void setBigBlind(float bigBlind) {
		this.bigBlind = bigBlind;
	}

	synchronized protected Turn getTurn() {
		return turn;
	}

	synchronized protected void setTurn(Turn turn) {
		this.turn = turn;
	}

	synchronized protected Lock getLock() {
		return lock;
	}

	synchronized protected void setLock(Lock lock) {
		this.lock = lock;
	}

	synchronized protected Group getGroup() {
		return group;
	}

	synchronized protected void setGroup(Group group) {
		this.group = group;
	}
}

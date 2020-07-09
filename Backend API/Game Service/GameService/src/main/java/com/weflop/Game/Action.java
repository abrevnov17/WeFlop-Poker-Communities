package com.weflop.Game;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.socket.WebSocketSession;

import com.weflop.Cards.Card;
import com.weflop.Database.DomainObjects.ActionPOJO;
import com.weflop.Database.DomainObjects.CardPOJO;

/**
 * Actions have an associated type and an optional parameter value
 * (as some actions such as raising have an associated float payload).
 * 
 * @author abrevnov
 *
 */
public class Action {
	// mandatory values
	private ActionType type;
	
	// optional values
	private String playerId; // user associated with action
	private WebSocketSession session; // session associated with action
	private Float value; // some actions have associated float as payload
	private List<Card> cards; // some actions have associated cards as payload
	
	// automatically set values
	private Instant timestamp;
	
	public Action(ActionType type, String playerId) {
		this.setType(type);
		this.setPlayerId(playerId);
		this.setTimestamp(Instant.now());
	}
	
	public Action(ActionType type, String playerId, float value) {
		this(type, playerId);
		this.setValue(value);
	}
	
	public Action(ActionType type, String playerId, List<Card> cards) {
		this(type, playerId);
		this.setCards(cards);
	}
	
	/**
	 * Provides POJO representation of Action
	 * (consumable by database).
	 * 
	 * @return Corresponding ActionPOJO instance
	 */
	public ActionPOJO toPojo() {
		List<CardPOJO> cards = this.cards.stream()
		        .map(card -> new CardPOJO(card.getSuit().getValue(), 
		        		card.getCardValue().getValue()))
		        .collect(Collectors.toList());
		return new ActionPOJO(type.getValue(), playerId, timestamp.toEpochMilli(), value, cards);
	}
	
	/* Getters and Setters */

	public ActionType getType() {
		return type;
	}

	public void setType(ActionType type) {
		this.type = type;
	}

	public Float getValue() {
		return value;
	}

	public void setValue(Float value) {
		this.value = value;
	}

	public String getPlayerId() {
		return playerId;
	}

	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}

	public WebSocketSession getSession() {
		return session;
	}

	public void setSession(WebSocketSession session) {
		this.session = session;
	}

	public Instant getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Instant timestamp) {
		this.timestamp = timestamp;
	}

	public List<Card> getCards() {
		return cards;
	}

	public void setCards(List<Card> cards) {
		this.cards = cards;
	}
	
	/**
	 * Returns a boolean indicating whether this action was
	 * created by a user (or, alternatively, an outgoing action generated
	 * by the game server).
	 * 
	 * @returns True if user action, false otherwise
	 */
	public boolean isUserAction() {
		return this.type != ActionType.DEAL;
	}
}

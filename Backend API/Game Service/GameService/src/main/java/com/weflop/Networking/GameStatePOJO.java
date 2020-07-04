package com.weflop.Networking;

import java.util.List;

import com.weflop.Database.DomainObjects.CardPOJO;
import com.weflop.Database.DomainObjects.PlayerPOJO;

/**
 * CRUD class containing properties that are propogated to individual players inside
 * of game packets.
 * 
 * @author abrevnov
 *
 */
public class GameStatePOJO {
	private String gameId;
	private List<CardPOJO> cards;
	private List<PlayerPOJO> p;
	private float pot;
	private List<CardPOJO> centerCards;
	private String idOfTurn;
	
	public GameStatePOJO(PlayerPOJO player, List<PlayerPOJO> otherPlayers) {
		this.cards = player.getCards();
	}
}

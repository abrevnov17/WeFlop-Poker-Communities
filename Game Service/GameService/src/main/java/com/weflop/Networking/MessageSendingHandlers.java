package com.weflop.Networking;

import java.io.IOException;
import java.util.List;

import org.springframework.web.socket.TextMessage;

import com.google.gson.JsonObject;
import com.weflop.Database.DomainObjects.CardPOJO;
import com.weflop.Database.DomainObjects.PlayerPOJO;
import com.weflop.Game.Action;
import com.weflop.Game.Group;
import com.weflop.Game.Player;

public class MessageSendingHandlers {
	public static void propogateAction(String gameId, Group group, Action action, int updateVersion) 
			throws InterruptedException, IOException {
		// creating message
		JsonObject message = new JsonObject();
		message.addProperty("game_id", gameId);
		message.addProperty("type", MessageType.ACTION.getValue());
		
		JsonObject payload = new JsonObject();

		payload.addProperty("type", action.getType().getValue());
		payload.addProperty("participant_id", action.getPlayerId());
		payload.addProperty("update", updateVersion);
		
		if (action.getValue() != null) {
			payload.addProperty("value", action.getValue());
		}
		
		message.add("payload", payload);
		
		String messageString = message.getAsString();
		
		// propogating message to players
		for (Player player : group.getPlayers()) {
			player.getSession().sendMessage(new TextMessage(messageString));
		}
		
		// propogating message to spectators
		for (Player spectator : group.getSpectators()) {
			spectator.getSession().sendMessage(new TextMessage(messageString));
		}
	}
	
	public static void sendGameState(String gameId, PlayerPOJO player, List<PlayerPOJO> players, 
			float pot, List<CardPOJO> centerCards, String idOfTurn) 
			throws InterruptedException, IOException {
		// creating message
		JsonObject message = new JsonObject();
		message.addProperty("game_id", gameId);
		message.addProperty("type", MessageType.GAME_STATE.getValue());

		JsonObject payload = new JsonObject();

		payload.addProperty("cards", WebSocketHandler.GSON.toJson(player.getCards()));
		payload.addProperty("center_cards", WebSocketHandler.GSON.toJson(player.getCards()));
		
		if (action.getValue() != null) {
			payload.addProperty("value", action.getValue());
		}
		
		message.add("payload", payload);
		
		String messageString = message.getAsString();
		
		// propogating message to players
		for (Player player : group.getPlayers()) {
			player.getSession().sendMessage(new TextMessage(messageString));
		}
		
		// propogating message to spectators
		for (Player spectator : group.getSpectators()) {
			spectator.getSession().sendMessage(new TextMessage(messageString));
		}
	}
}

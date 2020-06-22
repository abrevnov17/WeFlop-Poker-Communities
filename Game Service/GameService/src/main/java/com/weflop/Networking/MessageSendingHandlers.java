package com.weflop.Networking;

import java.io.IOException;

import org.springframework.web.socket.TextMessage;

import com.google.gson.JsonObject;
import com.weflop.Game.Action;
import com.weflop.Game.Group;
import com.weflop.Game.Player;

public class MessageSendingHandlers {
	public static void propogateAction(Group group, Action action) 
			throws InterruptedException, IOException {
		// creating message
		JsonObject message = new JsonObject();
		message.addProperty("type", action.getType().getValue());
		message.addProperty("participant_id", action.getPlayerId());
		
		if (action.getValue() != null) {
			message.addProperty("value", action.getValue());
		}
		
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

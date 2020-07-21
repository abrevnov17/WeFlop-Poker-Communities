package com.weflop.GameService.Networking;

import java.io.IOException;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.weflop.Game.Game;
import com.weflop.Game.GameFactory;

@Component
public class WebSocketHandler extends TextWebSocketHandler {

	public static final Gson GSON = new Gson();

	private static List<WebSocketSession> sessions = new CopyOnWriteArrayList<WebSocketSession>();

	@Override
	public void handleTextMessage(WebSocketSession session, TextMessage message)
			throws InterruptedException, IOException {
		JsonObject received = GSON.fromJson(message.getPayload(), JsonObject.class);

		MessageType type = MessageType.getTypeFromInt(received.get("type").getAsInt());

		// getting game from game id in message
		String gameId = received.get("game_id").getAsString();

		Game game = GameFactory.ID_TO_GAME.get(gameId);

		if (game == null) {
			session.sendMessage(new TextMessage("Invalid game id."));
		}

		JsonObject payload = received.get("payload").getAsJsonObject();

		switch (type) {
		case ACTION:
			MessageReceivingHandlers.handleAction(session, game, payload);
			break;
		default:
			System.out.println("INVALID COMMAND");
			break;
		}
	}

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		// the messages will be broadcasted to all users.
		sessions.add(session);
	}
}

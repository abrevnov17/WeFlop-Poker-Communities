package com.weflop.GameService.Networking;

import com.google.gson.JsonObject;
import com.weflop.Game.Action;
import com.weflop.Game.ActionType;
import com.weflop.Game.Game;

import java.io.IOException;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

public class MessageReceivingHandlers {

	public static void handleJoinGame(WebSocketSession session, Game game, JsonObject payload)
			throws InterruptedException, IOException {
		// parsing out properties
		String playerId = payload.get("user_id").getAsString();

		// storing user id and mapping session to id
		WebSocketHandler.sessionToPlayerId.put(session, playerId);

		// adding player to game
		try {
			game.performAction(new Action.ActionBuilder(ActionType.JOIN)
					.withPlayerId(playerId)
					.withSession(session)
					.build());
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("failing to join");
			session.sendMessage(new TextMessage("Error attempting to join game."));
		}

	}

	public static void handleAction(WebSocketSession session, Game game, JsonObject payload)
			throws InterruptedException, IOException {
		// parsing out properties
		ActionType type = ActionType.getTypeFromInt(payload.get("type").getAsInt());
		String playerId = WebSocketHandler.sessionToPlayerId.get(session);

		// handling various action types
		switch (type) {
		case START: {
			try {
				game.performAction(new Action.ActionBuilder(ActionType.START).withPlayerId(playerId).build());
			} catch (Exception e) {
				e.printStackTrace();
				session.sendMessage(new TextMessage("Error attempting to start game."));
			}
		}
			break;
		case CALL: {
			try {
				game.performAction(new Action.ActionBuilder(ActionType.CALL).withPlayerId(playerId).build());
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println(e.getMessage());
				System.out.println("Error attempting to call");
				session.sendMessage(new TextMessage("Error attempting to call."));
			}
		}
			break;
		case CHECK: {
			try {
				game.performAction(new Action.ActionBuilder(ActionType.CHECK).withPlayerId(playerId).build());
			} catch (Exception e) {
				e.printStackTrace();
				session.sendMessage(new TextMessage("Error attempting to check."));
			}
		}
			break;
		case FOLD: {
			try {
				game.performAction(new Action.ActionBuilder(ActionType.FOLD).withPlayerId(playerId).build());
			} catch (Exception e) {
				e.printStackTrace();
				session.sendMessage(new TextMessage("Error attempting to fold."));
			}
		}
			break;
		case RAISE: {
			try {
				float amount = payload.get("value").getAsFloat();
				game.performAction(new Action.ActionBuilder(ActionType.RAISE)
						.withPlayerId(playerId)
						.withValue(amount).build());
			} catch (Exception e) {
				e.printStackTrace();
				session.sendMessage(new TextMessage("Error attempting to raise."));
			}
		}
			break;
		case SIT: {
			try {
				int slot = payload.get("slot").getAsInt();
				float buyIn = payload.get("buy_in").getAsFloat();
				game.performAction(new Action.ActionBuilder(ActionType.SIT)
						.withPlayerId(playerId)
						.withSlot(slot)
						.withValue(buyIn)
						.build());
			} catch (Exception e) {
				e.printStackTrace();
				session.sendMessage(new TextMessage("Error attempting to sit."));
			}
		}
			break;
		case STAND: {
			try {
				game.performAction(new Action.ActionBuilder(ActionType.STAND).withPlayerId(playerId).build());
			} catch (Exception e) {
				e.printStackTrace();
				session.sendMessage(new TextMessage("Error attempting to stand."));
			}
		}
			break;
		default:
			session.sendMessage(new TextMessage("Unsupported action type"));
			break;
		}
	}
}

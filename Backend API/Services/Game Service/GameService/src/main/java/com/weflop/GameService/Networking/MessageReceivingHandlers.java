package com.weflop.GameService.Networking;

import com.google.gson.JsonObject;
import com.weflop.GameService.Game.Action;
import com.weflop.GameService.Game.ActionType;
import com.weflop.GameService.Game.Game;

import java.io.IOException;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

public class MessageReceivingHandlers {

	public static void handleAction(WebSocketSession session, Game game, JsonObject payload)
			throws InterruptedException, IOException {
		// parsing out properties
		ActionType type = ActionType.fromValue(payload.get("type").getAsString());
		String playerId = payload.get("user_id").getAsString();

		JsonObject error = new JsonObject(); // returned on error
		error.addProperty("type", "ERROR");
		
		// handling various action types
		switch (type) {
		case JOIN: {
			try {
				game.performAction(new Action.ActionBuilder(type)
						.withPlayerId(playerId)
						.withSession(session)
						.build());
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("failing to join");
				error.addProperty("error", e.getMessage());
				session.sendMessage(new TextMessage(error.toString()));
			}
		}
			break;
		case START: {
			try {
				game.performAction(new Action.ActionBuilder(type).withPlayerId(playerId).build());
			} catch (Exception e) {
				error.addProperty("error", e.getMessage());
				session.sendMessage(new TextMessage(error.toString()));
			}
		}
			break;
		case CALL: {
			try {
				game.performAction(new Action.ActionBuilder(type).withPlayerId(playerId).build());
			} catch (Exception e) {
				System.out.println("Error attempting to call");
				error.addProperty("error", e.getMessage());
				session.sendMessage(new TextMessage(error.toString()));
			}
		}
			break;
		case CHECK: {
			try {
				game.performAction(new Action.ActionBuilder(type).withPlayerId(playerId).build());
			} catch (Exception e) {
				e.printStackTrace();
				error.addProperty("error", e.getMessage());
				session.sendMessage(new TextMessage(error.toString()));
			}
		}
			break;
		case FOLD: {
			try {
				game.performAction(new Action.ActionBuilder(type).withPlayerId(playerId).build());
			} catch (Exception e) {
				e.printStackTrace();
				error.addProperty("error", e.getMessage());
				session.sendMessage(new TextMessage(error.toString()));
			}
		}
			break;
		case RAISE: {
			try {
				float amount = payload.get("value").getAsFloat();
				game.performAction(new Action.ActionBuilder(type)
						.withPlayerId(playerId)
						.withValue(amount).build());
			} catch (Exception e) {
				e.printStackTrace();
				error.addProperty("error", e.getMessage());
				session.sendMessage(new TextMessage(error.toString()));
			}
		}
			break;
		case ALL_IN: {
			try {
				game.performAction(new Action.ActionBuilder(type).withPlayerId(playerId).build());
			} catch (Exception e) {
				error.addProperty("error", e.getMessage());
				session.sendMessage(new TextMessage(error.toString()));
				e.printStackTrace();
			}
		}
			break;
		case SIT: {
			try {
				int slot = payload.get("slot").getAsInt();
				float buyIn = payload.get("buy_in").getAsFloat();
				game.performAction(new Action.ActionBuilder(type)
						.withPlayerId(playerId)
						.withSlot(slot)
						.withValue(buyIn)
						.build());
			} catch (Exception e) {
				e.printStackTrace();
				error.addProperty("error", e.getMessage());
				session.sendMessage(new TextMessage(error.toString()));
			}
		}
			break;
		case SIT_IN: {
			try {
				boolean posting = payload.get("enabled").getAsBoolean();
				game.performAction(new Action.ActionBuilder(type).withPlayerId(playerId).withEnabled(posting).build());
			} catch (Exception e) {
				e.printStackTrace();
				error.addProperty("error", e.getMessage());
				session.sendMessage(new TextMessage(error.toString()));
			}
		}
			break;
		case STAND: {
			try {
				game.performAction(new Action.ActionBuilder(type).withPlayerId(playerId).build());
			} catch (Exception e) {
				e.printStackTrace();
				error.addProperty("error", e.getMessage());
				session.sendMessage(new TextMessage(error.toString()));
			}
		}
			break;
		case SIT_OUT_HAND: {
			try {
				game.performAction(new Action.ActionBuilder(type).withPlayerId(playerId).build());
			} catch (Exception e) {
				e.printStackTrace();
				error.addProperty("error", e.getMessage());
				session.sendMessage(new TextMessage(error.toString()));
			}
		}
			break;
		case CANCEL_BUY_IN: {
			try {
				game.performAction(new Action.ActionBuilder(type).withPlayerId(playerId).build());
			} catch (Exception e) {
				e.printStackTrace();
				session.sendMessage(new TextMessage("Error attempting to cancel buy in."));
			}
		}
			break;
		case SIT_OUT_BB: {
			try {
				game.performAction(new Action.ActionBuilder(type).withPlayerId(playerId).build());
			} catch (Exception e) {
				e.printStackTrace();
				error.addProperty("error", e.getMessage());
				session.sendMessage(new TextMessage(error.toString()));
			}
		}
			break;
		case POST_BIG_BLIND: {
			try {
				game.performAction(new Action.ActionBuilder(type).withPlayerId(playerId).build());
			} catch (Exception e) {
				e.printStackTrace();
				error.addProperty("error", e.getMessage());
				session.sendMessage(new TextMessage(error.toString()));
			}
		}
			break;
		case TOP_OFF: {
			try {
				float amount = payload.get("amount").getAsFloat();
				game.performAction(new Action.ActionBuilder(type).withPlayerId(playerId).withValue(amount).build());
			} catch (Exception e) {
				e.printStackTrace();
				error.addProperty("error", e.getMessage());
				session.sendMessage(new TextMessage(error.toString()));
			}
		}
			break;
		case CHANGE_SEAT: {
			try {
				int slot = payload.get("slot").getAsInt();
				game.performAction(new Action.ActionBuilder(type).withPlayerId(playerId).withSlot(slot).build());
			} catch (Exception e) {
				e.printStackTrace();
				error.addProperty("error", e.getMessage());
				session.sendMessage(new TextMessage(error.toString()));
			}
		}
			break;
		case MUCK_CARDS: {
			try {
				game.performAction(new Action.ActionBuilder(type).withPlayerId(playerId).build());
			} catch (Exception e) {
				e.printStackTrace();
				error.addProperty("error", e.getMessage());
				session.sendMessage(new TextMessage(error.toString()));
			}
		}
			break;
		case SHOW_CARDS: {
			try {
				game.performAction(new Action.ActionBuilder(type).withPlayerId(playerId).build());
			} catch (Exception e) {
				e.printStackTrace();
				error.addProperty("error", e.getMessage());
				session.sendMessage(new TextMessage(error.toString()));
			}
		}
			break;
		case AUTO_CALL: {
			try {
				game.performAction(new Action.ActionBuilder(type).withPlayerId(playerId).build());
			} catch (Exception e) {
				e.printStackTrace();
				error.addProperty("error", e.getMessage());
				session.sendMessage(new TextMessage(error.toString()));
			}
		}
			break;
		case AUTO_CHECK_OR_FOLD: {
			try {
				game.performAction(new Action.ActionBuilder(type).withPlayerId(playerId).build());
			} catch (Exception e) {
				e.printStackTrace();
				error.addProperty("error", e.getMessage());
				session.sendMessage(new TextMessage(error.toString()));
			}
		}
			break;
		case AUTO_MUCK: {
			try {
				boolean enabled = payload.get("enabled").getAsBoolean();
				game.performAction(new Action.ActionBuilder(type)
						.withPlayerId(playerId)
						.withEnabled(enabled)
						.build());
			} catch (Exception e) {
				e.printStackTrace();
				error.addProperty("error", e.getMessage());
				session.sendMessage(new TextMessage(error.toString()));
			}
		}
			break;
		default:
			error.addProperty("error", "Unsupported action type.");
			session.sendMessage(new TextMessage(error.toString()));
			break;
		}
	}
}

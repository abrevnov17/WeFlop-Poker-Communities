package com.weflop.GameService.Networking;

import com.google.gson.JsonObject;
import com.weflop.Game.Action;
import com.weflop.Game.ActionType;
import com.weflop.Game.Game;

import java.io.IOException;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

public class MessageReceivingHandlers {

	public static void handleAction(WebSocketSession session, Game game, JsonObject payload)
			throws InterruptedException, IOException {
		// parsing out properties
		ActionType type = ActionType.fromValue(payload.get("type").getAsString());
		String playerId = payload.get("user_id").getAsString();

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
				session.sendMessage(new TextMessage("Error attempting to join game."));
			}
		}
			break;
		case START: {
			try {
				game.performAction(new Action.ActionBuilder(type).withPlayerId(playerId).build());
			} catch (Exception e) {
				e.printStackTrace();
				session.sendMessage(new TextMessage("Error attempting to start game."));
			}
		}
			break;
		case CALL: {
			try {
				game.performAction(new Action.ActionBuilder(type).withPlayerId(playerId).build());
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
				game.performAction(new Action.ActionBuilder(type).withPlayerId(playerId).build());
			} catch (Exception e) {
				e.printStackTrace();
				session.sendMessage(new TextMessage("Error attempting to check."));
			}
		}
			break;
		case FOLD: {
			try {
				game.performAction(new Action.ActionBuilder(type).withPlayerId(playerId).build());
			} catch (Exception e) {
				e.printStackTrace();
				session.sendMessage(new TextMessage("Error attempting to fold."));
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
				session.sendMessage(new TextMessage("Error attempting to raise."));
			}
		}
		case ALL_IN: {
			try {
				game.performAction(new Action.ActionBuilder(type).withPlayerId(playerId).build());
			} catch (Exception e) {
				e.printStackTrace();
				session.sendMessage(new TextMessage("Error attempting to go all-in."));
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
				session.sendMessage(new TextMessage("Error attempting to sit."));
			}
		}
			break;
		case STAND: {
			try {
				game.performAction(new Action.ActionBuilder(type).withPlayerId(playerId).build());
			} catch (Exception e) {
				e.printStackTrace();
				session.sendMessage(new TextMessage("Error attempting to stand."));
			}
		}
			break;
		case SIT_OUT_HAND: {
			try {
				game.performAction(new Action.ActionBuilder(type).withPlayerId(playerId).build());
			} catch (Exception e) {
				e.printStackTrace();
				session.sendMessage(new TextMessage("Error attempting to stand."));
			}
		}
			break;
		case SIT_OUT_BB: {
			try {
				game.performAction(new Action.ActionBuilder(type).withPlayerId(playerId).build());
			} catch (Exception e) {
				e.printStackTrace();
				session.sendMessage(new TextMessage("Error attempting to stand."));
			}
		}
			break;
		case POST_BIG_BLIND: {
			try {
				game.performAction(new Action.ActionBuilder(type).withPlayerId(playerId).build());
			} catch (Exception e) {
				e.printStackTrace();
				session.sendMessage(new TextMessage("Error attempting to stand."));
			}
		}
			break;
		case TOP_OFF: {
			try {
				float amount = payload.get("amount").getAsFloat();
				game.performAction(new Action.ActionBuilder(type).withPlayerId(playerId).withValue(amount).build());
			} catch (Exception e) {
				e.printStackTrace();
				session.sendMessage(new TextMessage("Error attempting to stand."));
			}
		}
			break;
		case CHANGE_SEAT: {
			try {
				int slot = payload.get("slot").getAsInt();
				game.performAction(new Action.ActionBuilder(type).withPlayerId(playerId).withSlot(slot).build());
			} catch (Exception e) {
				e.printStackTrace();
				session.sendMessage(new TextMessage("Error attempting to stand."));
			}
		}
			break;
		case MUCK_CARDS: {
			try {
				game.performAction(new Action.ActionBuilder(type).withPlayerId(playerId).build());
			} catch (Exception e) {
				e.printStackTrace();
				session.sendMessage(new TextMessage("Error attempting to muck hand."));
			}
		}
			break;
		case SHOW_CARDS: {
			try {
				game.performAction(new Action.ActionBuilder(type).withPlayerId(playerId).build());
			} catch (Exception e) {
				e.printStackTrace();
				session.sendMessage(new TextMessage("Error attempting to show hand."));
			}
		}
			break;
		case AUTO_CALL: {
			try {
				game.performAction(new Action.ActionBuilder(type).withPlayerId(playerId).build());
			} catch (Exception e) {
				e.printStackTrace();
				session.sendMessage(new TextMessage("Error attempting to set automatic call preference."));
			}
		}
			break;
		case AUTO_CHECK_OR_FOLD: {
			try {
				game.performAction(new Action.ActionBuilder(type).withPlayerId(playerId).build());
			} catch (Exception e) {
				e.printStackTrace();
				session.sendMessage(new TextMessage("Error attempting to set automatic check/fold preference."));
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
				session.sendMessage(new TextMessage("Error attempting to set automatic check/fold preference."));
			}
		}
			break;
		default:
			session.sendMessage(new TextMessage("Unsupported action type"));
			break;
		}
	}
}

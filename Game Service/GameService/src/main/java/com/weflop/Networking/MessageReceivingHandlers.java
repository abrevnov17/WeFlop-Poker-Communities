package com.weflop.Networking;

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
			game.performAction(new Action(ActionType.JOIN, playerId, session));
		} catch (Exception e) {
			session.sendMessage(new TextMessage("Error attempting to join game."));
		}
		
	}

	public static void handleAction(WebSocketSession session, Game game, JsonObject payload) 
			throws InterruptedException, IOException {
		// parsing out properties
		ActionType type = ActionType.getTypeFromInt(payload.get("type").getAsInt());
		String playerId = WebSocketHandler.sessionToPlayerId.get(session);
		
		// handling various action types
		switch(type) {
			case CALL: 
			{
				try {
					game.performAction(new Action(ActionType.CALL, playerId));
				} catch (Exception e) {
					session.sendMessage(new TextMessage("Error attempting to call."));
				}
			}
				break;
			case CHECK:
			{
				try {
					game.performAction(new Action(ActionType.CHECK, playerId));
				} catch (Exception e) {
					session.sendMessage(new TextMessage("Error attempting to check."));
				}
			}
				break;
			case FOLD:
			{
				try {
					game.performAction(new Action(ActionType.FOLD, playerId));
				} catch (Exception e) {
					session.sendMessage(new TextMessage("Error attempting to fold."));
				}
			}
				break;
			case RAISE:
			{
				try {
					float amount = payload.get("value").getAsFloat();
					game.performAction(new Action(ActionType.RAISE, playerId, amount));
				} catch (Exception e) {
					session.sendMessage(new TextMessage("Error attempting to raise."));
				}
			}
				break;
			case SIT:
			{
				try {
					game.performAction(new Action(ActionType.SIT, playerId));
				} catch (Exception e) {
					session.sendMessage(new TextMessage("Error attempting to sit."));
				}
			}
				break;
			case STAND:
			{
				try {
					game.performAction(new Action(ActionType.STAND, playerId));
				} catch (Exception e) {
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

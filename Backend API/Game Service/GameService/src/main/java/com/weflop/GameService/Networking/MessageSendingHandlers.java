package com.weflop.GameService.Networking;

import java.io.IOException;
import java.util.List;

import org.springframework.web.socket.TextMessage;

import com.google.gson.JsonObject;
import com.weflop.Game.Action;
import com.weflop.Game.Group;
import com.weflop.Game.Player;
import com.weflop.GameService.Database.DomainObjects.ActionPOJO;

public class MessageSendingHandlers {
	/**
	 * Propagates actions from incoming users to remaining users (assuming that they
	 * have already been processed and validated by the game server).
	 * 
	 * @param gameId
	 * @param group
	 * @param action
	 * @param updateVersion
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public static void propagateIncomingAction(String gameId, Group group, Action action, int epoch, List<Player> targets)
			throws InterruptedException, IOException {
		// creating message
		JsonObject message = new JsonObject();
		message.addProperty("game_id", gameId);
		message.addProperty("type", MessageType.ACTION.toValue());

		JsonObject payload = generatePayloadFromAction(action, epoch);

		message.add("payload", payload);

		String messageString = message.toString();

		// propagating message to targets
		for (Player participant : targets) {
			participant.getSession().sendMessage(new TextMessage(messageString));
		}
	}

	/**
	 * Propagates an action from game server sent to a specific player (encapsulated
	 * in playerId parameter of action).
	 * 
	 * @param gameId
	 * @param group
	 * @param action
	 * @param updateVersion
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public static void propagateOutgoingAction(String gameId, Action action, int epoch, List<Player> targets)
			throws InterruptedException, IOException {
		// creating message
		JsonObject message = new JsonObject();
		message.addProperty("game_id", gameId);
		message.addProperty("type", MessageType.ACTION.toValue());

		JsonObject payload = generatePayloadFromAction(action, epoch);
		
		message.add("payload", payload);

		String messageString = message.toString();

		// propagating message to targets
		for (Player participant : targets) {
			participant.getSession().sendMessage(new TextMessage(messageString));
		}
	}

	public static void sendGameState(Player target, GameStatePOJO gameState) throws InterruptedException, IOException {
		// creating message
		JsonObject message = new JsonObject();
		message.addProperty("type", MessageType.GAME_STATE.toValue());
		message.addProperty("payload", WebSocketHandler.GSON.toJson(gameState));
		
		String messageString = message.toString();

		// propagating state information to player
		target.getSession().sendMessage(new TextMessage(messageString));
	}

	public static void sendSynchronizationPackets(String gameId, Group group, int epoch, long turnTimeRemaining)
			throws InterruptedException, IOException {
		// creating message
		JsonObject message = new JsonObject();
		message.addProperty("game_id", gameId);
		message.addProperty("type", MessageType.SYNCHRONIZATION.toValue());

		JsonObject payload = new JsonObject();

		payload.addProperty("epoch", epoch);
		payload.addProperty("turn_time_remaining", turnTimeRemaining);

		message.add("payload", payload);

		String messageString = message.toString();

		// propagating message to all participants
		for (Player participant : group.getAllParticipants()) {
			participant.getSession().sendMessage(new TextMessage(messageString));
		}
	}
	
	private static JsonObject generatePayloadFromAction(Action action, int epoch) {
		ActionPOJO actionPOJO = action.toPojo();
		
		JsonObject payload = new JsonObject();

		payload.addProperty("type", actionPOJO.getType());
		payload.addProperty("epoch", epoch);

		if (action.getCards() != null) {
			payload.addProperty("cards", WebSocketHandler.GSON.toJson(actionPOJO.getCards()));
		}
		
		if (action.getPlayerIds() != null) {
			payload.addProperty("players", WebSocketHandler.GSON.toJson(action.getPlayerIds()));
		}
		
		if (action.getPlayerId() != null) {
			payload.addProperty("partipant_id", action.getPlayerId());
		}
		
		if (action.getValue() != null) {
			payload.addProperty("value", action.getValue());
		}
		
		if (action.getPots() != null) {
			payload.addProperty("pots", WebSocketHandler.GSON.toJson(action.getPots()));
		}
		
		return payload;
	}
}

package com.weflop.GameService.Networking;

import java.io.IOException;
import java.util.List;

import org.springframework.web.socket.TextMessage;

import com.google.gson.JsonObject;
import com.weflop.GameService.Database.DomainObjects.ActionPOJO;
import com.weflop.GameService.Game.Action;
import com.weflop.GameService.Game.Group;
import com.weflop.GameService.Game.Player;

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
		sendMessageToTargets(messageString, targets);
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
		sendMessageToTargets(messageString, targets);
	}

	public static void sendGameState(Player target, GameStatePOJO gameState) throws InterruptedException, IOException {
		// creating message
		JsonObject message = new JsonObject();
		message.addProperty("type", MessageType.GAME_STATE.toValue());
		message.add("payload", WebSocketHandler.GSON.toJsonTree(gameState));
		
		String messageString = message.toString();

		// propagating state information to player
		if (target.getSession().isOpen()) {
			target.getSession().sendMessage(new TextMessage(messageString));
		}
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
		sendMessageToTargets(messageString, group.getAllParticipants());
	}
	
	private static JsonObject generatePayloadFromAction(Action action, int epoch) {
		ActionPOJO actionPOJO = action.toPojo();
		
		JsonObject payload = new JsonObject();

		payload.addProperty("type", actionPOJO.getType());
		payload.addProperty("epoch", epoch);

		if (action.getCards() != null) {
			payload.add("cards", WebSocketHandler.GSON.toJsonTree(actionPOJO.getCards()));
		}
		
		if (action.getPlayerIds() != null) {
			payload.add("players", WebSocketHandler.GSON.toJsonTree(action.getPlayerIds()));
		}
		
		if (action.getPlayerId() != null) {
			payload.addProperty("partipant_id", action.getPlayerId());
		}
		
		if (action.getValue() != null) {
			payload.addProperty("value", action.getValue());
		}

		if (action.getSlot() != null) {
			payload.addProperty("slot", action.getSlot());
		}
		
		if (action.getPots() != null) {
			payload.add("pots", WebSocketHandler.GSON.toJsonTree(action.getPots()));
		}
		
		if (action.getLimitedPlayers() != null) {
			payload.add("limited_players", WebSocketHandler.GSON.toJsonTree(action.getLimitedPlayers()));
		}
		
		if (action.getEnabled() != null) {
			payload.add("enabled", WebSocketHandler.GSON.toJsonTree(action.getEnabled()));
		}

		if (action.getDuration() != null) {
			payload.addProperty("duration", action.getDuration());
		}

		return payload;
	}
	
	/**
	 * Takes a message and sends it to a list of players (or spectators).
	 * 
	 * @param messageString
	 * @param targets
	 */
	private static void sendMessageToTargets(String messageString, List<Player> targets) 
			throws InterruptedException, IOException { 
		// propagating message to targets
		for (Player participant : targets) {
			// check that participant websocket connection is open and send message if it is
			if (participant.getSession().isOpen()) {
				participant.getSession().sendMessage(new TextMessage(messageString));
			}
		}
	}
}

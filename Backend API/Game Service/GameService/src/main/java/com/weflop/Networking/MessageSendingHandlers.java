package com.weflop.Networking;

import java.io.IOException;

import org.springframework.web.socket.TextMessage;

import com.google.gson.JsonObject;
import com.weflop.Database.DomainObjects.ActionPOJO;
import com.weflop.Game.Action;
import com.weflop.Game.Group;
import com.weflop.Game.Player;

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
	public static void propagateIncomingAction(String gameId, Group group, Action action, int epoch)
			throws InterruptedException, IOException {
		// creating message
		JsonObject message = new JsonObject();
		message.addProperty("game_id", gameId);
		message.addProperty("type", MessageType.ACTION.getValue());

		JsonObject payload = new JsonObject();

		payload.addProperty("type", action.getType().getValue());
		payload.addProperty("participant_id", action.getPlayerId());
		payload.addProperty("epoch", epoch);

		if (action.getValue() != null) {
			payload.addProperty("value", action.getValue());
		}

		message.add("payload", payload);

		String messageString = message.getAsString();

		// propagating message to players
		for (Player player : group.getPlayers()) {
			player.getSession().sendMessage(new TextMessage(messageString));
		}

		// propagating message to spectators
		for (Player spectator : group.getSpectators()) {
			spectator.getSession().sendMessage(new TextMessage(messageString));
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
	public static void propagateOutgoingActionToPlayer(String gameId, Player player, Action action, int epoch)
			throws InterruptedException, IOException {
		ActionPOJO actionPOJO = action.toPojo();

		// creating message
		JsonObject message = new JsonObject();
		message.addProperty("game_id", gameId);
		message.addProperty("type", MessageType.ACTION.getValue());

		JsonObject payload = new JsonObject();

		payload.addProperty("type", actionPOJO.getType());
		payload.addProperty("epoch", epoch);

		if (action.getCards() != null) {
			payload.addProperty("cards", WebSocketHandler.GSON.toJson(actionPOJO.getCards()));
		}

		message.add("payload", payload);

		String messageString = message.getAsString();

		// propagating message to players
		player.getSession().sendMessage(new TextMessage(messageString));
	}

	/**
	 * Propagates outgoing message to group of players in game.
	 * 
	 * @param gameId
	 * @param group
	 * @param action
	 * @param epoch
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public static void propagateOutgoingAction(String gameId, Group group, Action action, int epoch)
			throws InterruptedException, IOException {
		for (Player player : group.getPlayers()) {
			propagateOutgoingActionToPlayer(gameId, player, action, epoch);
		}
	}

	public static void sendGameState(Player player, GameStatePOJO gameState) throws InterruptedException, IOException {
		// creating message
		JsonObject message = new JsonObject();
		message.addProperty("type", MessageType.GAME_STATE.getValue());
		message.addProperty("payload", WebSocketHandler.GSON.toJson(gameState));

		String messageString = message.getAsString();

		// propogating state information to player
		player.getSession().sendMessage(new TextMessage(messageString));
	}

	public static void sendSynchronizationPackets(String gameId, Group group, int epoch, long turnTimeRemaining)
			throws InterruptedException, IOException {
		// creating message
		JsonObject message = new JsonObject();
		message.addProperty("game_id", gameId);
		message.addProperty("type", MessageType.SYNCHRONIZATION.getValue());

		JsonObject payload = new JsonObject();

		payload.addProperty("epoch", epoch);
		payload.addProperty("turn_time_remaining", turnTimeRemaining);

		message.add("payload", payload);

		String messageString = message.getAsString();

		// propagating message to players
		for (Player player : group.getPlayers()) {
			player.getSession().sendMessage(new TextMessage(messageString));
		}

		// propagating message to spectators
		for (Player spectator : group.getSpectators()) {
			spectator.getSession().sendMessage(new TextMessage(messageString));
		}
	}
}

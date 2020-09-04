package com.weflop.GameService.Game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.util.Assert;

import com.weflop.Evaluation.HandRank;

/**
 * Class acts as a wrapper for all betting information. All bets should go through
 * this class.
 * 
 * @author abrevnov
 */
public class BetController {

	private float roundBet;

	private float lastRaise;
	private Player lastRaisePlayer;

	private Ledger ledger;

	private float totalPot;

	private float smallBlind;
	private float bigBlind;

	private float minBuyIn;
	private float maxBuyIn;

	public BetController(float smallBlind, float bigBlind, float minBuyIn, float maxBuyIn) {
		this.roundBet = 0.0f;
		this.lastRaise = 0.0f;
		this.setLedger(new Ledger());
		this.setTotalPot(0);
		this.smallBlind = smallBlind;
		this.bigBlind = bigBlind;
		this.minBuyIn = minBuyIn;
		this.maxBuyIn = maxBuyIn;
		this.lastRaisePlayer = null;
	}

	public BetController(float smallBlind, float bigBlind, float minBuyIn, float maxBuyIn, Ledger ledger) {
		this(smallBlind, bigBlind, minBuyIn, maxBuyIn);
		this.ledger = ledger;
	}


	/* Betting Methods */

	synchronized public void bet(Player player, float bet) {
		player.bet(bet); // verification performed in 'bet' method
	}

	/**
	 * Takes in a player and a total bet that is meant to be a raise.
	 * Verifies that the bet raises by sufficient amount and places
	 * said bet if valid. Throws runtime exception otherwise.
	 * 
	 * @param player
	 * @param bet
	 */
	synchronized public void raise(Player player, float bet) {
		float amountRaised = bet - roundBet;

		Assert.isTrue(player.getRoundBet() + bet > this.roundBet,
				"You have to raise more than the prior bet");
		Assert.isTrue(amountRaised >= 2.00f * lastRaise, "Insufficient raise amount");
		Assert.isTrue(bet >= this.smallBlind, "Any bet must be at least as large as small blind.");

		player.bet(bet); // verification performed in 'bet' method
		lastRaise = amountRaised;
		lastRaisePlayer = player;
	}

	/**
	 * Takes player all in and returns amount they contributed.
	 * @param player
	 * @return Amount player contributed to pot.
	 */
	synchronized public float goAllIn(Player player) {
		return player.goAllIn();
	}

	synchronized public void paySmallBlind(Player player) {
		bet(player, smallBlind);
	}

	synchronized public void payBigBlind(Player player) {
		bet(player, bigBlind);
	}

	/**
	 * Posts big blinds for each player in given list of players.
	 * @param playersPostingBigBlind
	 */
	synchronized public void postBigBlinds(List<Player> playersPostingBigBlind) {		
		for (Player player : playersPostingBigBlind) {
			if (player.getRoundBet() != bigBlind) {
				bet(player, bigBlind);
			}
		}
	}

	/**
	 * Pays blinds. Returns a list of actions that should be propagated as messages to users.
	 * @param smallBlindPlayer (Null if there is no small blind)
	 * @param bigBlindPlayer
	 * @return List of actions to be propagated to participants of group.
	 */
	synchronized public List<Propagatable> payBlinds(Player smallBlindPlayer, Player bigBlindPlayer) {
		List<Propagatable> propagatables = new ArrayList<Propagatable>();

		if (smallBlindPlayer != null) {
			// otherwise, we are in a normal situation where small blind pays blind
			paySmallBlind(smallBlindPlayer);
			Action smallBlindAction = new Action.ActionBuilder(ActionType.SMALL_BLIND).withPlayerId(smallBlindPlayer.getId()).build();
			propagatables.add(new Propagatable(smallBlindAction));
		}

		// big blind pays
		payBigBlind(bigBlindPlayer);
		bigBlindPlayer.updateCurrentAndFutureState(PlayerState.WAITING_FOR_TURN, PlayerState.WAITING_FOR_TURN);
		Action bigBlindAction = new Action.ActionBuilder(ActionType.BIG_BLIND).withPlayerId(bigBlindPlayer.getId()).build();
		propagatables.add(new Propagatable(bigBlindAction));

		return propagatables;
	}

	synchronized public void buyIn(Player player, float amount) {
		Assert.isTrue(amount >= minBuyIn && amount <= maxBuyIn, 
				"Buy in must be between 10 and 200 BBs");

		player.increaseBalance(amount);
		addPlayerToLedger(player.getId()); // adding player to ledger (if not already present)
	}

	/* Ledger Update Methods */

	/**
	 * Adds player to ledger (safe operation if player already present in ledger).
	 * @param playerId
	 */
	synchronized public void addPlayerToLedger(String playerId) {
		ledger.updateEntry(playerId, 0.00f); // Note: this doesn't overwrite existing value
	}

	/* Pot / Distribution Methods */

	/**
	 * Given a group, returns a list of pots. This should only be called at the end of rounds of betting.
	 */
	synchronized public List<Pot> endOfBettingRoundGeneratePots(Group group) {
		List<Pot> pots = new ArrayList<Pot>(); // list of all pots

		// we subtract everyone's bets for this hand from the ledger
		for (Player player : group.getActivePlayersInHand()) {
			this.ledger.updateEntry(player.getId(), -player.getHandBet());
		}

		List<Player> players = new ArrayList<Player>(group.getActivePlayersInBettingRound());

		// sorts players by their current bet in increasing order
		Collections.sort(players, Comparator.comparingDouble(Player :: getHandBet));

		// while players with chips exist, we match players to side pots
		float offset = 0;
		while (players.size() > 0) {
			Pot pot = new Pot();

			Player minStackPlayer = players.remove(0); // removing player with minimum currentBet
			float minStack = minStackPlayer.getHandBet();

			// updating pot
			pot.setSize((minStack-offset) * players.size());
			pot.addAllPlayers(players);

			// removing all players with no more chips left after contributing to the current pot
			players = players.stream().filter(player -> player.getHandBet() != minStack).collect(Collectors.toList());

			// updating total pot
			this.totalPot += minStack-offset;
			
			offset += minStack;
		}

		return pots;
	}

	/**
	 * Distributes pots to winners. Returns a list of propagatables to be propagated to group.
	 */
	public List<Propagatable> distributePots(Group group, List<Pot> pots) {
		List<Propagatable> propagatables = new ArrayList<Propagatable>();
		
		Set<Player> playersForcedToShowCards = new HashSet<Player>();
		Set<Player> playersWithOptionToMuck = new HashSet<Player>();

		for (Pot pot : pots) {
			List<Player> playersWithMaxRank = new ArrayList<Player>();
	
			HandRank maxRank = null;
			int startingSlot = lastRaisePlayer != null ? lastRaisePlayer.getSlot() : group.getSmallBlindIndex();
	
			for (Player player : group.getPlayersClockwiseFromSlot(startingSlot)) {
				if (pot.getPlayers().contains(player)) {
					HandRank rank = player.getHand().getRank();
					if (maxRank == null || rank.compareTo(maxRank) == 0) {
						// either first hand rank we have found or tied with best hand rank we have found
						maxRank = rank;
						playersWithMaxRank.add(player);
	
						// forced to show cards
						playersForcedToShowCards.add(player);
					} else if (rank.compareTo(maxRank) > 0) {
						// hand rank is best we have found so far
						playersWithMaxRank.clear();
						maxRank = rank;
						playersWithMaxRank.add(player);
	
						// forced to show cards
						playersForcedToShowCards.add(player);
					} else {
						// given option to muck cards
						playersWithOptionToMuck.add(player);
					}
				}
			}
	
			// distribute funds to winner(s)
			float perPlayerWinnings = pot.getSize() / playersWithMaxRank.size(); // split pot between winners
			for (Player player : playersWithMaxRank) {
				System.out.println("Player with id: " + player.getId() + " has won winnings: " + perPlayerWinnings);
				player.increaseBalance(perPlayerWinnings);
				ledger.updateEntry(player.getId(), perPlayerWinnings);
			}
	
			// propagating updates
			propagatables.add(new Propagatable(
					new Action.ActionBuilder(ActionType.POT_WON)
					.withPlayerIds(playersWithMaxRank.stream().map(player -> player.getId()).collect(Collectors.toList()))
					.withValue(pot.getSize())
					.build()));
		}
		
		playersWithOptionToMuck.removeAll(playersForcedToShowCards); // these sets should be mutually exclusive

		for (Player player : playersForcedToShowCards) {
			propagatables.add(new Propagatable(
					new Action.ActionBuilder(ActionType.SHOW_CARDS)
					.withPlayerId(player.getId())
					.withCards(player.getHand().getCards())
					.build()));
		}
		
		for (Player player : playersWithOptionToMuck) {
			if (player.getSettings().isAutoMuckEnabled()) {
				propagatables.add(new Propagatable(new Action.ActionBuilder(ActionType.MUCK_CARDS).withPlayerId(player.getId()).build()));
				continue;
			}
			group.getPlayersWhoCanMuck().add(player);
			propagatables.add(new Propagatable(
					new Action.ActionBuilder(ActionType.OPTION_TO_SHOW_CARDS)
					.build(), player));
		}
		
		return propagatables;
	}

	/* Reset Methods */

	/**
	 * Resets betting state for new hand.
	 */
	public void resetForNewHand() {
		resetForNewBettingRound();
		this.totalPot = 0.00f;
	}

	/**
	 * Reset betting state for new betting round.
	 */
	public void resetForNewBettingRound() {
		this.roundBet = 0.00f;
		this.lastRaise = 0.00f;
	}

	/* Getters and Setters */

	public float getRoundBet() {
		return roundBet;
	}

	public void setRoundBet(float roundBet) {
		this.roundBet = roundBet;
	}

	public float getLastRaise() {
		return lastRaise;
	}

	public void setLastRaise(float lastRaise) {
		this.lastRaise = lastRaise;
	}

	public Ledger getLedger() {
		return ledger;
	}

	public void setLedger(Ledger ledger) {
		this.ledger = ledger;
	}

	public float getTotalPot() {
		return totalPot;
	}

	public void setTotalPot(float totalPot) {
		this.totalPot = totalPot;
	}
}

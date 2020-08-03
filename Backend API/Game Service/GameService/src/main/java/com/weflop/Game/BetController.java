package com.weflop.Game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

		Assert.isTrue(player.getCurrentRoundBet() + bet > this.roundBet,
				"You have to raise more than the prior bet");
		Assert.isTrue(amountRaised >= 2.00f * lastRaise, "Insufficient raise amount");
		Assert.isTrue(bet >= this.smallBlind, "Any bet must be at least as large as small blind.");

		player.bet(bet); // verification performed in 'bet' method
		lastRaise = amountRaised;
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
	
	synchronized public void buyIn(Player player, float amount) {
		Assert.isTrue(amount >= minBuyIn && amount <= maxBuyIn, 
				"Buy in must be between 10 and 200 BBs");
		
		player.setBalance(amount);
	}
	
	/* Ledger Update Methods */

	/**
	 * Adds player to ledger (safe operation if player already present in ledger).
	 * @param playerId
	 */
	synchronized public void addPlayerToLedger(String playerId) {
		ledger.updateEntry(playerId, 0.00f);
	}

	/* Pot / Distribution Methods */

	/**
	 * Given a list of active players (have not folded and are playing in the current round),
	 * returns a list of Pot's. This should only be called at the end of rounds of betting.
	 */
	synchronized public List<Pot> endOfBettingRoundGeneratePots(List<Player> activePlayers) {
		List<Pot> pots = new ArrayList<Pot>(); // list of all pots

		List<Player> players = new ArrayList<Player>(activePlayers);
		// we subtract everyone's bets for this hand from the ledger
		for (Player player : players) {
			this.ledger.updateEntry(player.getId(), -player.getCurrentBet());
		}

		// sorts players by their current bet in increasing order
		Collections.sort(players, Comparator.comparingDouble(Player :: getCurrentBet));

		// while players with chips exist, we match players to side pots and distribute
		// chips to winners of pots:
		while (players.size() > 0) {
			Pot pot = new Pot();

			Player minStackPlayer = players.get(0); // peeking player with minimum currentBet
			float minStack = minStackPlayer.getCurrentBet();

			// updating pot
			pot.setSize(minStack * players.size());
			pot.addAllPlayers(players);

			for (Player player : players) {
				player.setCurrentBet(player.getCurrentBet() - minStack);
			}

			// removing all players with no more chips left after contributing to the current pot
			players = players.stream().filter(player -> player.getCurrentBet() > 0).collect(Collectors.toList());

			// updating total pot
			this.totalPot += minStack;
		}

		return pots;
	}

	/**
	 * Distributes pots to winners. Returns a list of actions to be propagated to group.
	 */
	synchronized public List<Action> distributePots(List<Pot> pots) {
		List<Action> actions = new ArrayList<Action>();

		for (Pot pot : pots) {
			float potSize = pot.getSize();
			List<Player> playersWithMaxRank = getPlayersWithMaxRank(pot.getPlayers());

			// distribute funds to winner(s)
			float perPlayerWinnings = potSize / playersWithMaxRank.size(); // split pot between winners
			for (Player player : playersWithMaxRank) {
				player.increaseBalance(perPlayerWinnings);
				ledger.updateEntry(player.getId(), perPlayerWinnings);
			}

			// propogating update
			actions.add(new Action.ActionBuilder(ActionType.POT_WON)
					.withPlayerIds(playersWithMaxRank.stream().map(player -> player.getId()).collect(Collectors.toList()))
					.withValue(potSize)
					.build());
		}

		return actions;
	}

	/**
	 * From a set of active players, gets all the players with the maximum
	 * hand-rank in the group (it is a list due to possiblity of ties).
	 * @param players
	 * @return List of players with max hand-rank
	 */
	private List<Player> getPlayersWithMaxRank(Set<Player> players) {
		List<Player> playersWithMaxRank = new ArrayList<Player>();

		HandRank maxRank = null;
		for (Player player : players) {
			HandRank rank = player.getHand().getRank();
			if (maxRank == null || rank.compareTo(maxRank) == 0) {
				// either first hand rank we have found or tied with best hand rank we have found
				maxRank = rank;
				playersWithMaxRank.add(player);
			} else if (rank.compareTo(maxRank) > 0) {
				// hand rank is best we have found so far
				playersWithMaxRank.clear();
				maxRank = rank;
				playersWithMaxRank.add(player);
			}
		}

		return playersWithMaxRank;
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

package com.weflop.GameService.Game;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Ledger keeps track of the amount won or lost for every
 * player who has sat at a table.
 * 
 * @author abrevnov
 */
public class Ledger {
	private Map<String, Float> ledger;
	
	public Ledger() {
		ledger = new ConcurrentHashMap<String, Float>();
	}
	
	public static Ledger fromPOJO(Map<String, Float> pojo) {
		Ledger ledger = new Ledger();
		ledger.ledger.putAll(pojo);
		return ledger;
	}
	
	/**
	 * Updates ledger entry for given player by a given amount
	 * (can be positive or negative). If entry does not exist,
	 * creates a new one.
	 * 
	 * @param playerId
	 * @param betAmount
	 */
	public void updateEntry(String playerId, float delta) {
		if (ledger.containsKey(playerId)) {
			float oldValue = ledger.get(playerId);
			ledger.put(playerId, oldValue + delta);
		} else {
			ledger.put(playerId, delta);
		}
	}

	public Map<String, Float> toPOJO() {
		return ledger;
	}
}

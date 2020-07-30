package com.weflop.GameService.REST;

import java.util.Map;

import com.weflop.Game.GameCustomMetadata;

/**
 * POJO class wrapping game metadata exposed by our REST API.
 * 
 * @author abrevnov
 */
public class GameMetadata {
	private long startTimestamp;

	private float pot;
	
	private GameCustomMetadata customMetadata;
	private Map<String, Float> ledger;
	
	public GameMetadata(long startTimestamp, float pot, GameCustomMetadata customMetadata,
			Map<String, Float> ledger) {
		this.startTimestamp = startTimestamp;
		this.pot = pot;
		this.customMetadata = customMetadata;
		this.ledger = ledger;
	}

	public long getStartTimestamp() {
		return startTimestamp;
	}

	public void setStartTimestamp(long startTimestamp) {
		this.startTimestamp = startTimestamp;
	}

	public float getPot() {
		return pot;
	}

	public void setPot(float pot) {
		this.pot = pot;
	}

	public GameCustomMetadata getCustomMetadata() {
		return customMetadata;
	}

	public void setCustomMetadata(GameCustomMetadata customMetadata) {
		this.customMetadata = customMetadata;
	}

	public Map<String, Float> getLedger() {
		return ledger;
	}

	public void setLedger(Map<String, Float> ledger) {
		this.ledger = ledger;
	}
	
}

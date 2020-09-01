package com.weflop.GameService.Game;

/**
 * Wrapper for all customizable player settings.
 * @author abrevnov
 *
 */
public class PlayerSettings {
	
	private boolean autoMuckEnabled;
	
	public PlayerSettings() {
		this.setAutoMuckEnabled(false);
	}

	public boolean isAutoMuckEnabled() {
		return autoMuckEnabled;
	}

	public void setAutoMuckEnabled(boolean autoMuckEnabled) {
		this.autoMuckEnabled = autoMuckEnabled;
	}
}

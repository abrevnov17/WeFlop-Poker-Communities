package com.weflop.GameService.Game;

import java.util.ArrayList;
import java.util.List;

/**
 * Wrapper for actions that can be propagated to users.
 * Note: A null targets list indicates that action should be
 * propagated to group.
 * 
 * @author abrevnov
 */
public class Propagatable {

	private Action action;
	private List<Player> targets;
	
	public Propagatable(Action action) {
		this.setAction(action);
		this.setTargets(null);
	}
	
	public Propagatable(Action action, Player participant) {
		this.setAction(action);
		this.setTargets(new ArrayList<Player>());
		this.targets.add(participant);
	}
	
	public Propagatable(Action action, List<Player> targets) {
		this.setAction(action);
		this.setTargets(targets);
	}

	public Action getAction() {
		return action;
	}

	public void setAction(Action action) {
		this.action = action;
	}

	public List<Player> getTargets() {
		return targets;
	}

	public void setTargets(List<Player> targets) {
		this.targets = targets;
	}
}

package com.weflop.Game;

import java.util.List;
import java.util.stream.Collectors;

import com.weflop.Database.DomainObjects.ActionPOJO;
import com.weflop.Database.DomainObjects.HistoryPOJO;

/**
 * Describes history of game.
 */
public class History {

	private InitialState initState;

	private List<Action> actionsSequence;

	public History(InitialState initState, List<Action> actionsSequence) {
		super();
		this.initState = initState;
		this.actionsSequence = actionsSequence;
	}

	/**
	 * Converts to POJO Representation.
	 */
	public HistoryPOJO toPOJO() {
		List<ActionPOJO> actions = this.actionsSequence.stream().map(action -> action.toPojo())
				.collect(Collectors.toList());
		return new HistoryPOJO(initState.toPOJO(), actions);
	}

	public void appendActionToSequence(Action action) {
		this.actionsSequence.add(action);
	}

	/* Getters and Setters */

	public InitialState getInitState() {
		return initState;
	}

	public void setInitState(InitialState initState) {
		this.initState = initState;
	}

	public List<Action> getActionsSequence() {
		return actionsSequence;
	}

	public void setActionsSequence(List<Action> actionsSequence) {
		this.actionsSequence = actionsSequence;
	}
}

package com.weflop.GameService.Database.DomainObjects;

import java.util.List;

/**
 * CRUD object wrapping information about game history.
 */
public class HistoryPOJO {

	private InitialStatePOJO initState;

	private List<ActionPOJO> actionsSequence;

	public HistoryPOJO(InitialStatePOJO initState, List<ActionPOJO> actionsSequence) {
		super();
		this.initState = initState;
		this.actionsSequence = actionsSequence;
	}

	public InitialStatePOJO getInitState() {
		return initState;
	}

	public void setInitState(InitialStatePOJO initState) {
		this.initState = initState;
	}

	public List<ActionPOJO> getActionsSequence() {
		return actionsSequence;
	}

	public void setActionsSequence(List<ActionPOJO> actionsSequence) {
		this.actionsSequence = actionsSequence;
	}
}

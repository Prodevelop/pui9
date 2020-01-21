package es.prodevelop.pui9.eventlistener.event;

import es.prodevelop.pui9.common.model.dto.interfaces.IPuiVariable;

/**
 * Event for Variable updated
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class VariableUpdatedEvent extends PuiEvent<IPuiVariable> {

	private static final long serialVersionUID = 1L;

	private String oldValue;

	public VariableUpdatedEvent(IPuiVariable variable, String oldValue) {
		super(variable, "variableUpdated");
		this.oldValue = oldValue;
	}

	public String getOldValue() {
		return oldValue;
	}

}

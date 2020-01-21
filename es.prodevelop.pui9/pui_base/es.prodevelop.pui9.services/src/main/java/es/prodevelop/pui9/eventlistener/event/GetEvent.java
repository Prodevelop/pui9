package es.prodevelop.pui9.eventlistener.event;

import es.prodevelop.pui9.model.dto.interfaces.ITableDto;

/**
 * Event for the Get action
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class GetEvent extends PuiEvent<ITableDto> {

	private static final long serialVersionUID = 1L;

	public GetEvent(ITableDto dto) {
		super(dto, "get");
	}

}

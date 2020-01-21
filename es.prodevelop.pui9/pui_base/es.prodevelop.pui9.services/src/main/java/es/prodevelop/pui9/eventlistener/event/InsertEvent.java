package es.prodevelop.pui9.eventlistener.event;

import es.prodevelop.pui9.model.dto.interfaces.ITableDto;

/**
 * Event for the Insert action
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class InsertEvent extends PuiEvent<ITableDto> {

	private static final long serialVersionUID = 1L;

	public InsertEvent(ITableDto dto) {
		super(dto, "insert");
	}

}

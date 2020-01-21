package es.prodevelop.pui9.eventlistener.event;

import es.prodevelop.pui9.model.dto.interfaces.ITableDto;

/**
 * Event for the Delete action
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class DeleteEvent extends PuiEvent<ITableDto> {

	private static final long serialVersionUID = 1L;

	public DeleteEvent(ITableDto dto) {
		super(dto, "delete");
	}

}

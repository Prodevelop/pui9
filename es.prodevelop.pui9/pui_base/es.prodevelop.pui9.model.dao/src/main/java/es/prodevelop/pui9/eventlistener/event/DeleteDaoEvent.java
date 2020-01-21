package es.prodevelop.pui9.eventlistener.event;

import es.prodevelop.pui9.model.dto.interfaces.ITableDto;

/**
 * Event for the DAO Delete action
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class DeleteDaoEvent extends AbstractDaoEvent<ITableDto> {

	private static final long serialVersionUID = 1L;

	public DeleteDaoEvent(ITableDto dto) {
		super(dto, "delete_dao");
	}

}

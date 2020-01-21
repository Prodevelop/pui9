package es.prodevelop.pui9.eventlistener.event;

import es.prodevelop.pui9.model.dto.interfaces.ITableDto;

/**
 * Event for the DAO Insert action
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class InsertDaoEvent extends AbstractDaoEvent<ITableDto> {

	private static final long serialVersionUID = 1L;

	public InsertDaoEvent(ITableDto dto) {
		super(dto, "insert_dao");
	}

}

package es.prodevelop.pui9.eventlistener.event;

import es.prodevelop.pui9.model.dto.interfaces.ITableDto;

/**
 * Event for the DAO Update action
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class UpdateDaoEvent extends AbstractDaoEvent<ITableDto> {

	private static final long serialVersionUID = 1L;

	private ITableDto oldDto;

	public UpdateDaoEvent(ITableDto dto, ITableDto oldDto) {
		super(dto, "update_dao");
		this.oldDto = oldDto;
	}

	public ITableDto getOldDto() {
		return oldDto;
	}

}

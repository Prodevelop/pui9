package es.prodevelop.pui9.eventlistener.event;

import es.prodevelop.pui9.model.dto.interfaces.IDto;
import es.prodevelop.pui9.model.dto.interfaces.ITableDto;

/**
 * Event for the Update action
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class UpdateEvent extends PuiEvent<ITableDto> {

	private static final long serialVersionUID = 1L;

	private IDto oldDto;

	public UpdateEvent(ITableDto dto, ITableDto oldDto) {
		super(dto, "update");
		this.oldDto = oldDto;
	}

	public IDto getOldDto() {
		return oldDto;
	}

}

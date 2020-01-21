package es.prodevelop.pui9.eventlistener.event;

import es.prodevelop.pui9.model.dto.interfaces.ITableDto;

/**
 * Event for the Patch action
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class PatchEvent extends PuiEvent<ITableDto> {

	private static final long serialVersionUID = 1L;

	private ITableDto oldDto;

	public PatchEvent(ITableDto dto, ITableDto oldDto) {
		super(dto, "patch");
		this.oldDto = oldDto;
	}

	public ITableDto getOldDto() {
		return oldDto;
	}

}

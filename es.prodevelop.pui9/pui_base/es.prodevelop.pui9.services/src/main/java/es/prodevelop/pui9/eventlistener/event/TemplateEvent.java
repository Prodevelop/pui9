package es.prodevelop.pui9.eventlistener.event;

import es.prodevelop.pui9.model.dto.interfaces.ITableDto;

/**
 * Event for the Template (new object) action
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class TemplateEvent extends PuiEvent<ITableDto> {

	private static final long serialVersionUID = 1L;

	public TemplateEvent(ITableDto dto) {
		super(dto, "template");
	}

}

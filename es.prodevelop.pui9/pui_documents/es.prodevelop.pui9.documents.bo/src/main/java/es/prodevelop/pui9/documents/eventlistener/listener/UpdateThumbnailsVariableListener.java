package es.prodevelop.pui9.documents.eventlistener.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.prodevelop.pui9.documents.service.interfaces.IPuiDocumentService;
import es.prodevelop.pui9.eventlistener.event.VariableUpdatedEvent;
import es.prodevelop.pui9.eventlistener.listener.PuiListener;
import es.prodevelop.pui9.exceptions.PuiException;

/**
 * Listener fired when the variable 'DOCUMENTS_THUMBNAILS_VALUES' is updated
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
@Component
public class UpdateThumbnailsVariableListener extends PuiListener<VariableUpdatedEvent> {

	private static final String DOCUMENTS_THUMBNAILS_VALUES_VAR = "DOCUMENTS_THUMBNAILS_VALUES";

	@Autowired
	private IPuiDocumentService documentService;

	@Override
	protected boolean passFilter(VariableUpdatedEvent event) {
		return event.getSource().getVariable().equals(DOCUMENTS_THUMBNAILS_VALUES_VAR)
				&& !event.getOldValue().equals(event.getSource().getValue());
	}

	@Override
	protected void process(VariableUpdatedEvent event) throws PuiException {
		documentService.reloadThumbnails();
	}

}

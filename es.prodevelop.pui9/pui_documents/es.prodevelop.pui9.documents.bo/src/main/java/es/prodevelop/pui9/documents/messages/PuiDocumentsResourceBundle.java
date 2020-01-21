package es.prodevelop.pui9.documents.messages;

import java.util.HashMap;
import java.util.Map;

import es.prodevelop.pui9.documents.exceptions.PuiDocumentsExtensionsException;
import es.prodevelop.pui9.documents.exceptions.PuiDocumentsFileSizeException;
import es.prodevelop.pui9.documents.exceptions.PuiDocumentsThumbnailException;
import es.prodevelop.pui9.documents.exceptions.PuiDocumentsUpdateException;
import es.prodevelop.pui9.documents.exceptions.PuiDocumentsUploadException;
import es.prodevelop.pui9.messages.AbstractPuiListResourceBundle;

/**
 * More specific implementation of {@link AbstractPuiListResourceBundle} for PUI
 * Documents component
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public abstract class PuiDocumentsResourceBundle extends AbstractPuiListResourceBundle {

	@Override
	protected Map<Object, String> getMessages() {
		Map<Object, String> messages = new HashMap<>();
		messages.put(PuiDocumentsExtensionsException.CODE, getExtensionsMessage_401());
		messages.put(PuiDocumentsFileSizeException.CODE, getFileSizeMessage_402());
		messages.put(PuiDocumentsThumbnailException.CODE, getThumbnailMessage_403());
		messages.put(PuiDocumentsUploadException.CODE, getUploadMessage_404());
		messages.put(PuiDocumentsUpdateException.CODE, getUpdateMessage_405());

		return messages;
	}

	protected abstract String getExtensionsMessage_401();

	protected abstract String getFileSizeMessage_402();

	protected abstract String getThumbnailMessage_403();

	protected abstract String getUploadMessage_404();

	protected abstract String getUpdateMessage_405();

}

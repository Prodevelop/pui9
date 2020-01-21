package es.prodevelop.pui9.documents.messages;

import es.prodevelop.pui9.messages.AbstractPuiMessages;

/**
 * Utility class to get internationalized messages for PUI Documents
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class PuiDocumentsMessages extends AbstractPuiMessages {

	private static PuiDocumentsMessages singleton;

	public static PuiDocumentsMessages getSingleton() {
		if (singleton == null) {
			singleton = new PuiDocumentsMessages();
		}
		return singleton;
	}

	private PuiDocumentsMessages() {
	}

	@Override
	protected Class<?> getResourceBundleClass() {
		return PuiDocumentsResourceBundle.class;
	}

}

package es.prodevelop.pui9.docgen.messages;

import es.prodevelop.pui9.messages.AbstractPuiMessages;

/**
 * Utility class to get internationalized messages for PUI Docgen
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class PuiDocgenMessages extends AbstractPuiMessages {

	private static PuiDocgenMessages singleton;

	public static PuiDocgenMessages getSingleton() {
		if (singleton == null) {
			singleton = new PuiDocgenMessages();
		}
		return singleton;
	}

	private PuiDocgenMessages() {
	}

	@Override
	protected Class<?> getResourceBundleClass() {
		return PuiDocgenResourceBundle.class;
	}

}

package es.prodevelop.pui9.common.messages;

import es.prodevelop.pui9.messages.AbstractPuiMessages;

/**
 * Utility class to get internationalized messages for PUI Common
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class PuiCommonMessages extends AbstractPuiMessages {

	private static PuiCommonMessages singleton;

	public static PuiCommonMessages getSingleton() {
		if (singleton == null) {
			singleton = new PuiCommonMessages();
		}
		return singleton;
	}

	private PuiCommonMessages() {
	}

	@Override
	protected Class<?> getResourceBundleClass() {
		return PuiCommonResourceBundle.class;
	}

}

package es.prodevelop.pui9.controllers.messages;

import es.prodevelop.pui9.messages.AbstractPuiMessages;

/**
 * Utility class to get internationalized messages for PUI Common
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class PuiControllersMessages extends AbstractPuiMessages {

	private static PuiControllersMessages singleton;

	public static PuiControllersMessages getSingleton() {
		if (singleton == null) {
			singleton = new PuiControllersMessages();
		}
		return singleton;
	}

	private PuiControllersMessages() {
	}

	@Override
	protected Class<?> getResourceBundleClass() {
		return PuiControllersResourceBundle.class;
	}

}

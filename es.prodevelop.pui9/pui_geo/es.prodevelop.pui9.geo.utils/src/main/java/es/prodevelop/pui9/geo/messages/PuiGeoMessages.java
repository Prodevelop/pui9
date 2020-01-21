package es.prodevelop.pui9.geo.messages;

import es.prodevelop.pui9.messages.AbstractPuiMessages;

/**
 * Utility class to get internationalized messages for PUI GEO
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class PuiGeoMessages extends AbstractPuiMessages {

	private static PuiGeoMessages singleton;

	public static PuiGeoMessages getSingleton() {
		if (singleton == null) {
			singleton = new PuiGeoMessages();
		}
		return singleton;
	}

	private PuiGeoMessages() {
	}

	@Override
	protected Class<?> getResourceBundleClass() {
		return PuiGeoResourceBundle.class;
	}

}

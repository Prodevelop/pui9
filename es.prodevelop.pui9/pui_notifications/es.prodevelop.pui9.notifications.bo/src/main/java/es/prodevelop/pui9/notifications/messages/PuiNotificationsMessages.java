package es.prodevelop.pui9.notifications.messages;

import es.prodevelop.pui9.messages.AbstractPuiMessages;

/**
 * Utility class to get internationalized messages for PUI Notifications
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class PuiNotificationsMessages extends AbstractPuiMessages {

	private static PuiNotificationsMessages singleton;

	public static PuiNotificationsMessages getSingleton() {
		if (singleton == null) {
			singleton = new PuiNotificationsMessages();
		}
		return singleton;
	}

	private PuiNotificationsMessages() {
	}

	@Override
	protected Class<?> getResourceBundleClass() {
		return PuiNotificationsResourceBundle.class;
	}

}

package es.prodevelop.pui9.notifications.messages;

import java.util.HashMap;
import java.util.Map;

import es.prodevelop.pui9.messages.AbstractPuiListResourceBundle;
import es.prodevelop.pui9.notifications.exceptions.PuiNotificationsAnonymousNotAllowedException;

/**
 * More specific implementation of {@link AbstractPuiListResourceBundle} for PUI
 * Notifications component
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public abstract class PuiNotificationsResourceBundle extends AbstractPuiListResourceBundle {

	@Override
	protected Map<Object, String> getMessages() {
		Map<Object, String> messages = new HashMap<>();
		messages.put(PuiNotificationsAnonymousNotAllowedException.CODE, getAnonymousNotAllowed_701());

		return messages;
	}

	protected abstract String getAnonymousNotAllowed_701();

}

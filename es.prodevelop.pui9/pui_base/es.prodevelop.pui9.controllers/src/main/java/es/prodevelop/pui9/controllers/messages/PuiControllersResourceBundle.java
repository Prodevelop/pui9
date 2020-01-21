package es.prodevelop.pui9.controllers.messages;

import java.util.HashMap;
import java.util.Map;

import es.prodevelop.pui9.controllers.exceptions.PuiControllersFromJsonException;
import es.prodevelop.pui9.controllers.exceptions.PuiControllersToJsonException;
import es.prodevelop.pui9.messages.AbstractPuiListResourceBundle;

/**
 * More specific implementation of {@link AbstractPuiListResourceBundle} for PUI
 * Common component
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public abstract class PuiControllersResourceBundle extends AbstractPuiListResourceBundle {

	@Override
	protected Map<Object, String> getMessages() {
		Map<Object, String> messages = new HashMap<>();

		// Exceptions
		messages.put(PuiControllersFromJsonException.CODE, getFromJsonException_801());
		messages.put(PuiControllersToJsonException.CODE, getToJsonException_802());

		return messages;
	}

	protected abstract String getFromJsonException_801();

	protected abstract String getToJsonException_802();

}

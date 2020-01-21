package es.prodevelop.pui9.geo.messages;

import java.util.HashMap;
import java.util.Map;

import es.prodevelop.pui9.geo.exceptions.PuiGeoCoordinatesException;
import es.prodevelop.pui9.geo.exceptions.PuiGeoInitGeoServerException;
import es.prodevelop.pui9.messages.AbstractPuiListResourceBundle;

/**
 * More specific implementation of {@link AbstractPuiListResourceBundle} for PUI
 * GEO component
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public abstract class PuiGeoResourceBundle extends AbstractPuiListResourceBundle {

	@Override
	protected Map<Object, String> getMessages() {
		Map<Object, String> messages = new HashMap<>();
		messages.put(PuiGeoCoordinatesException.CODE, getCoordinatesMessage_601());
		messages.put(PuiGeoInitGeoServerException.CODE, getInitGeoServerMessage_602());

		return messages;
	}

	protected abstract String getCoordinatesMessage_601();

	protected abstract String getInitGeoServerMessage_602();

}

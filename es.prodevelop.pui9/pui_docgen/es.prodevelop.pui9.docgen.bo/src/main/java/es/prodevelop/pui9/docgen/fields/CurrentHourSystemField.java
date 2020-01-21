package es.prodevelop.pui9.docgen.fields;

import java.time.Instant;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;

import es.prodevelop.pui9.login.PuiUserSession;
import es.prodevelop.pui9.utils.PuiDateUtil;

/**
 * A System Field that represents the current Time in HH:mm format
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
@Component
public class CurrentHourSystemField implements ISystemField {

	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

	private CurrentHourSystemField() {
		SystemFieldsRegistry.getSingleton().registerSystemField(this);
	}

	@Override
	public String getName() {
		return "CURRENT_HOUR";
	}

	@Override
	public String getValue() {
		if (PuiUserSession.getCurrentSession() != null) {
			return PuiDateUtil.temporalAccessorToString(Instant.now(),
					formatter.withZone(PuiUserSession.getCurrentSession().getTimezone()));
		} else {
			return PuiDateUtil.temporalAccessorToString(Instant.now(), formatter);
		}
	}

}

package es.prodevelop.pui9.docgen.fields;

import java.time.Instant;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;

import es.prodevelop.pui9.login.PuiUserSession;
import es.prodevelop.pui9.utils.PuiDateUtil;

/**
 * A System Field that represents the current Date in yyyy/MM/dd format, unless
 * there is a session opened, and it takes it date format
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
@Component
public class CurrentDateSystemField implements ISystemField {

	private DateTimeFormatter defaultFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");

	private CurrentDateSystemField() {
		SystemFieldsRegistry.getSingleton().registerSystemField(this);
	}

	@Override
	public String getName() {
		return "CURRENT_DATE";
	}

	@Override
	public String getValue() {
		DateTimeFormatter formatter = defaultFormatter;
		if (PuiUserSession.getCurrentSession() != null) {
			formatter = DateTimeFormatter.ofPattern(PuiUserSession.getCurrentSession().getDateformat())
					.withZone(PuiUserSession.getCurrentSession().getTimezone());
		}
		return PuiDateUtil.temporalAccessorToString(Instant.now(), formatter);
	}

}

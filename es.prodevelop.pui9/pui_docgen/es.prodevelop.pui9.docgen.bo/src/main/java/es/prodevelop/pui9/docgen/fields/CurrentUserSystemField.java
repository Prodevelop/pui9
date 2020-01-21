package es.prodevelop.pui9.docgen.fields;

import org.springframework.stereotype.Component;

import es.prodevelop.pui9.login.PuiUserSession;

/**
 * A System Field that represents the current logged user
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
@Component
public class CurrentUserSystemField implements ISystemField {

	private CurrentUserSystemField() {
		SystemFieldsRegistry.getSingleton().registerSystemField(this);
	}

	@Override
	public String getName() {
		return "CURRENT_USER";
	}

	@Override
	public String getValue() {
		return PuiUserSession.getCurrentSession() != null ? PuiUserSession.getCurrentSession().getName() : null;
	}

}

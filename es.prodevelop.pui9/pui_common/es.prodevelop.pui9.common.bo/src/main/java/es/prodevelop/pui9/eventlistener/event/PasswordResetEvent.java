package es.prodevelop.pui9.eventlistener.event;

import es.prodevelop.pui9.common.model.dto.interfaces.IPuiUser;

/**
 * Event for Request a Reset of the Password
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class PasswordResetEvent extends PuiEvent<IPuiUser> {

	private static final long serialVersionUID = 1L;

	public PasswordResetEvent(IPuiUser puiUser) {
		super(puiUser, "passwordReset");
	}

}

package es.prodevelop.pui9.eventlistener.event;

import es.prodevelop.pui9.login.PuiUserSession;

/**
 * Adapter for Login into the application
 */
public class SigninEvent extends PuiEvent<PuiUserSession> {

	private static final long serialVersionUID = 1L;

	public SigninEvent(PuiUserSession session) {
		super(session, "signin");
	}

}

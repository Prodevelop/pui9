package es.prodevelop.pui9.eventlistener.event;

import es.prodevelop.pui9.login.PuiUserSession;

/**
 * Adapter for Logout from application
 */
public class SignoutEvent extends PuiEvent<PuiUserSession> {

	private static final long serialVersionUID = 1L;

	public SignoutEvent(PuiUserSession session) {
		super(session, "signout");
	}

}

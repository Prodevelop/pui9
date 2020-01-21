package es.prodevelop.pui9.eventlistener.event;

import es.prodevelop.pui9.common.model.dto.interfaces.IPuiUser;

/**
 * Event for Request a Reset of the Password
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class RequestResetPasswordEvent extends PuiEvent<IPuiUser> {

	private static final long serialVersionUID = 1L;

	public RequestResetPasswordEvent(IPuiUser puiUser) {
		super(puiUser, "requestResetPassword");
	}

}

package es.prodevelop.pui9.notifications.exceptions;

import org.apache.http.HttpStatus;

public class PuiNotificationsAnonymousNotAllowedException extends AbstractPuiNotificationsException {

	private static final long serialVersionUID = 1L;

	public static final Integer CODE = 701;

	public PuiNotificationsAnonymousNotAllowedException() {
		super(CODE);
	}

	@Override
	public int getStatusResponse() {
		return HttpStatus.SC_UNAUTHORIZED;
	}

}

package es.prodevelop.pui9.notifications.exceptions;

import es.prodevelop.pui9.exceptions.PuiServiceException;
import es.prodevelop.pui9.notifications.messages.PuiNotificationsMessages;

public abstract class AbstractPuiNotificationsException extends PuiServiceException {

	private static final long serialVersionUID = 1L;

	public AbstractPuiNotificationsException(Exception cause, Integer code) {
		super(cause, code, cause.getMessage());
	}

	public AbstractPuiNotificationsException(Integer code) {
		this(code, new Object[0]);
	}

	public AbstractPuiNotificationsException(Integer code, Object... parameters) {
		super(code, PuiNotificationsMessages.getSingleton().getString(code), parameters);
	}

}

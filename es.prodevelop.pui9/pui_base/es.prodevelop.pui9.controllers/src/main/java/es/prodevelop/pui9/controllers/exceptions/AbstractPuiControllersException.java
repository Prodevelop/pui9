package es.prodevelop.pui9.controllers.exceptions;

import es.prodevelop.pui9.controllers.messages.PuiControllersMessages;
import es.prodevelop.pui9.exceptions.PuiException;

public abstract class AbstractPuiControllersException extends PuiException {

	private static final long serialVersionUID = 1L;

	public AbstractPuiControllersException(Exception cause, Integer code) {
		super(cause, code, cause.getMessage());
	}

	public AbstractPuiControllersException(Integer code) {
		this(code, new Object[0]);
	}

	public AbstractPuiControllersException(Integer code, Object... parameters) {
		super(null, code, PuiControllersMessages.getSingleton().getString(code), parameters);
	}

}

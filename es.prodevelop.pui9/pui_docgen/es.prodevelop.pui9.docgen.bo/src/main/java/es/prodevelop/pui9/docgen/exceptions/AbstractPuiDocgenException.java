package es.prodevelop.pui9.docgen.exceptions;

import es.prodevelop.pui9.docgen.messages.PuiDocgenMessages;
import es.prodevelop.pui9.exceptions.PuiServiceException;

public abstract class AbstractPuiDocgenException extends PuiServiceException {

	private static final long serialVersionUID = 1L;

	public AbstractPuiDocgenException(Exception cause, Integer code) {
		super(cause, code, cause.getMessage());
	}

	public AbstractPuiDocgenException(Integer code) {
		this(code, new Object[0]);
	}

	public AbstractPuiDocgenException(Integer code, Object... parameters) {
		super(code, PuiDocgenMessages.getSingleton().getString(code), parameters);
	}

}

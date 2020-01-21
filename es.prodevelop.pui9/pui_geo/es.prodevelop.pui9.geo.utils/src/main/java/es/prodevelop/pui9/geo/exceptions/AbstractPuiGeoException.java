package es.prodevelop.pui9.geo.exceptions;

import es.prodevelop.pui9.exceptions.PuiException;
import es.prodevelop.pui9.geo.messages.PuiGeoMessages;

public abstract class AbstractPuiGeoException extends PuiException {

	private static final long serialVersionUID = 1L;

	public AbstractPuiGeoException(Exception cause) {
		this(cause, null);
	}

	public AbstractPuiGeoException(Exception cause, Integer code) {
		super(cause, code, PuiGeoMessages.getSingleton().getString(code));
	}

}

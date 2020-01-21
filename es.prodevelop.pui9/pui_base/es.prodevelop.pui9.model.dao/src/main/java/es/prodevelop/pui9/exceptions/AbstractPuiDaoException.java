package es.prodevelop.pui9.exceptions;

import es.prodevelop.pui9.messages.PuiDaoMessages;

/**
 * Abstract exception for DAO exceptions. More specific exceptions should
 * provide more detailed information about the fired error
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public abstract class AbstractPuiDaoException extends PuiException {

	private static final long serialVersionUID = 1L;

	public AbstractPuiDaoException(Integer code) {
		this(code, new Object[0]);
	}

	public AbstractPuiDaoException(Integer code, Object... parameters) {
		this(null, code, parameters);
	}

	public AbstractPuiDaoException(Exception cause) {
		super(cause);
	}

	public AbstractPuiDaoException(Exception cause, Integer code, Object... parameters) {
		super(cause, code, PuiDaoMessages.getSingleton().getString(code), parameters);
	}

}

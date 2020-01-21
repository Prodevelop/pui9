package es.prodevelop.pui9.common.exceptions;

import es.prodevelop.pui9.common.messages.PuiCommonMessages;
import es.prodevelop.pui9.exceptions.PuiServiceException;

/**
 * Abstract exception for Common exceptions. More specific exceptions should
 * provide more detailed information about the fired error
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public abstract class AbstractPuiCommonException extends PuiServiceException {

	private static final long serialVersionUID = 1L;

	public AbstractPuiCommonException(Integer code) {
		this(code, new Object[0]);
	}

	public AbstractPuiCommonException(Integer code, Exception cause) {
		super(cause, code, PuiCommonMessages.getSingleton().getString(code));
	}

	public AbstractPuiCommonException(Integer code, Object... parameters) {
		super(code, PuiCommonMessages.getSingleton().getString(code), parameters);
	}

}

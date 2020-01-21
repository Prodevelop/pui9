package es.prodevelop.pui9.documents.exceptions;

import es.prodevelop.pui9.documents.messages.PuiDocumentsMessages;
import es.prodevelop.pui9.exceptions.PuiServiceException;

public abstract class AbstractPuiDocumentsException extends PuiServiceException {

	private static final long serialVersionUID = 1L;

	public AbstractPuiDocumentsException(Integer code) {
		super(code, PuiDocumentsMessages.getSingleton().getString(code));
	}

	public AbstractPuiDocumentsException(Exception cause, Integer code) {
		super(cause, code, PuiDocumentsMessages.getSingleton().getString(code));
	}

}

package es.prodevelop.pui9.documents.exceptions;

import org.apache.http.HttpStatus;

public class PuiDocumentsExtensionsException extends AbstractPuiDocumentsException {

	private static final long serialVersionUID = 1L;

	public static final Integer CODE = 401;

	public PuiDocumentsExtensionsException() {
		super(CODE);
	}

	@Override
	public int getStatusResponse() {
		return HttpStatus.SC_UNPROCESSABLE_ENTITY;
	}

}

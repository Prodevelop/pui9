package es.prodevelop.pui9.documents.exceptions;

import org.apache.http.HttpStatus;

public class PuiDocumentsUploadException extends AbstractPuiDocumentsException {

	private static final long serialVersionUID = 1L;

	public static final Integer CODE = 404;

	public PuiDocumentsUploadException() {
		super(CODE);
	}

	public PuiDocumentsUploadException(Exception cause) {
		super(cause, CODE);
	}

	@Override
	public int getStatusResponse() {
		return HttpStatus.SC_UNPROCESSABLE_ENTITY;
	}

}

package es.prodevelop.pui9.documents.exceptions;

import org.apache.http.HttpStatus;

public class PuiDocumentsThumbnailException extends AbstractPuiDocumentsException {

	private static final long serialVersionUID = 1L;

	public static final Integer CODE = 403;

	public PuiDocumentsThumbnailException() {
		super(CODE);
	}

	@Override
	public int getStatusResponse() {
		return HttpStatus.SC_UNPROCESSABLE_ENTITY;
	}

}

package es.prodevelop.pui9.docgen.exceptions;

import org.apache.http.HttpStatus;

public class PuiDocgenUploadingTemplateException extends AbstractPuiDocgenException {

	private static final long serialVersionUID = 1L;

	public static final Integer CODE = 503;

	public PuiDocgenUploadingTemplateException() {
		super(CODE);
	}

	@Override
	public int getStatusResponse() {
		return HttpStatus.SC_UNPROCESSABLE_ENTITY;
	}

}

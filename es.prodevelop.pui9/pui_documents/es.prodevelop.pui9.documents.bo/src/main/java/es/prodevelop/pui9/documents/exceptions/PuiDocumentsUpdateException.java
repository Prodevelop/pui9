package es.prodevelop.pui9.documents.exceptions;

public class PuiDocumentsUpdateException extends AbstractPuiDocumentsException {

	private static final long serialVersionUID = 1L;

	public static final Integer CODE = 405;

	public PuiDocumentsUpdateException() {
		super(CODE);
	}

}

package es.prodevelop.pui9.docgen.exceptions;

public class PuiDocgenModelNotExistsException extends AbstractPuiDocgenException {

	private static final long serialVersionUID = 1L;

	public static final Integer CODE = 504;

	public PuiDocgenModelNotExistsException(String viewName) {
		super(CODE, viewName);
	}

}

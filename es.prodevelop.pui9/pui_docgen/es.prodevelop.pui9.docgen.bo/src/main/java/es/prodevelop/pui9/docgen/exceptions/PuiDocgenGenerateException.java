package es.prodevelop.pui9.docgen.exceptions;

public class PuiDocgenGenerateException extends AbstractPuiDocgenException {

	private static final long serialVersionUID = 1L;

	public static final Integer CODE = 505;

	public PuiDocgenGenerateException(Exception cause) {
		super(cause, CODE);
	}

}

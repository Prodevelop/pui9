package es.prodevelop.pui9.docgen.exceptions;

public class PuiDocgenNoParserException extends AbstractPuiDocgenException {

	private static final long serialVersionUID = 1L;

	public static final Integer CODE = 502;

	public PuiDocgenNoParserException(String fileName) {
		super(CODE, fileName);
	}

}

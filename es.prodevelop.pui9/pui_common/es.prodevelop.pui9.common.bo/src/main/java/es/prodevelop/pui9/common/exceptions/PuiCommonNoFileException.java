package es.prodevelop.pui9.common.exceptions;

public class PuiCommonNoFileException extends AbstractPuiCommonException {

	private static final long serialVersionUID = 1L;

	public static final Integer CODE = 206;

	public PuiCommonNoFileException() {
		super(CODE);
	}

}

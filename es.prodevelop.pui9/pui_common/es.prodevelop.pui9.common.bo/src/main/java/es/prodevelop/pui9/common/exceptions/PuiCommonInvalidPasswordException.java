package es.prodevelop.pui9.common.exceptions;

public class PuiCommonInvalidPasswordException extends AbstractPuiCommonException {

	private static final long serialVersionUID = 1L;

	public static final Integer CODE = 202;

	public PuiCommonInvalidPasswordException() {
		super(CODE);
	}

}

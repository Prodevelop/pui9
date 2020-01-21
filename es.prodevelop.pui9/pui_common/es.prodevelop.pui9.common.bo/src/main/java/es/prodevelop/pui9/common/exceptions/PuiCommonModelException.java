package es.prodevelop.pui9.common.exceptions;

public class PuiCommonModelException extends AbstractPuiCommonException {

	private static final long serialVersionUID = 1L;

	public static final Integer CODE = 205;

	public PuiCommonModelException(String model) {
		super(CODE, model);
	}

}

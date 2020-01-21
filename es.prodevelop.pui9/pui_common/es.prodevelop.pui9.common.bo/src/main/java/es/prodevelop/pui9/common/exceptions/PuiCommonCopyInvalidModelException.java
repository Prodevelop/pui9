package es.prodevelop.pui9.common.exceptions;

public class PuiCommonCopyInvalidModelException extends AbstractPuiCommonException {

	private static final long serialVersionUID = 1L;

	public static final Integer CODE = 219;

	public PuiCommonCopyInvalidModelException(String model) {
		super(CODE, model);
	}

}

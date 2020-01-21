package es.prodevelop.pui9.common.exceptions;

import org.apache.http.HttpStatus;

public class PuiCommonIncorrectLoginException extends AbstractPuiCommonException {

	private static final long serialVersionUID = 1L;

	public static final Integer CODE = 204;

	public PuiCommonIncorrectLoginException() {
		super(CODE);
	}

	public PuiCommonIncorrectLoginException(Exception cause) {
		super(CODE, cause);
	}

	@Override
	public int getStatusResponse() {
		return HttpStatus.SC_UNAUTHORIZED;
	}

}

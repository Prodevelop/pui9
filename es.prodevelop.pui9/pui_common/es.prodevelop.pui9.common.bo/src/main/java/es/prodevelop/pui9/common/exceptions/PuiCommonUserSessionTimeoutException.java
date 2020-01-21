package es.prodevelop.pui9.common.exceptions;

import org.apache.http.HttpStatus;

public class PuiCommonUserSessionTimeoutException extends AbstractPuiCommonException {

	private static final long serialVersionUID = 1L;

	public static final Integer CODE = 209;

	public PuiCommonUserSessionTimeoutException() {
		super(CODE);
	}

	@Override
	public int getStatusResponse() {
		return HttpStatus.SC_UNAUTHORIZED;
	}

	@Override
	public boolean shouldLog() {
		return false;
	}

}

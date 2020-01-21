package es.prodevelop.pui9.common.exceptions;

import org.apache.http.HttpStatus;

public class PuiCommonUserDisabledException extends AbstractPuiCommonException {

	private static final long serialVersionUID = 1L;

	public static final Integer CODE = 210;

	public PuiCommonUserDisabledException(String user) {
		super(CODE, user);
	}

	@Override
	public int getStatusResponse() {
		return HttpStatus.SC_UNAUTHORIZED;
	}

}

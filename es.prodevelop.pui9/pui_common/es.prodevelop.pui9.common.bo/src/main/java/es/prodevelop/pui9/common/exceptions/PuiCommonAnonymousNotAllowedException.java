package es.prodevelop.pui9.common.exceptions;

import org.apache.http.HttpStatus;

public class PuiCommonAnonymousNotAllowedException extends AbstractPuiCommonException {

	private static final long serialVersionUID = 1L;

	public static final Integer CODE = 201;

	public PuiCommonAnonymousNotAllowedException() {
		super(CODE);
	}

	@Override
	public int getStatusResponse() {
		return HttpStatus.SC_UNAUTHORIZED;
	}

}

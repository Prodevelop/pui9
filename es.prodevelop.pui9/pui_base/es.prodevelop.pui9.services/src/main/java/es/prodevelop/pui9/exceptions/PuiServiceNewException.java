package es.prodevelop.pui9.exceptions;

public class PuiServiceNewException extends PuiServiceException {

	private static final long serialVersionUID = 1L;

	public PuiServiceNewException(PuiServiceException cause) {
		super(cause, cause.getMessage());
	}

	public PuiServiceNewException(Exception cause) {
		super(cause);
	}

}

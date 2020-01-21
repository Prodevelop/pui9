package es.prodevelop.pui9.exceptions;

public class PuiServiceGetException extends PuiServiceException {

	private static final long serialVersionUID = 1L;

	public PuiServiceGetException() {
		super(null);
	}

	public PuiServiceGetException(PuiServiceException cause) {
		super(cause, cause.getMessage());
	}

	public PuiServiceGetException(AbstractPuiDaoException cause) {
		super(cause);
	}

	public PuiServiceGetException(Exception cause) {
		super(cause);
	}

}

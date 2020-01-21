package es.prodevelop.pui9.exceptions;

public class PuiServiceInsertException extends PuiServiceException {

	private static final long serialVersionUID = 1L;

	public PuiServiceInsertException(PuiServiceException cause) {
		super(cause, cause.getMessage());
	}

	public PuiServiceInsertException(AbstractPuiDaoException cause) {
		super(cause);
	}

}

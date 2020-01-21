package es.prodevelop.pui9.exceptions;

public class PuiServiceDeleteException extends PuiServiceException {

	private static final long serialVersionUID = 1L;

	public PuiServiceDeleteException(PuiServiceException cause) {
		super(cause, cause.getMessage());
	}

	public PuiServiceDeleteException(AbstractPuiDaoException cause) {
		super(cause);
	}

}

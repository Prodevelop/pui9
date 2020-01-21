package es.prodevelop.pui9.exceptions;

public class PuiServiceUpdateException extends PuiServiceException {

	private static final long serialVersionUID = 1L;

	public PuiServiceUpdateException(PuiServiceException cause) {
		super(cause, cause.getInternalCode(), cause.getMessage());
	}

	public PuiServiceUpdateException(AbstractPuiDaoException cause) {
		super(cause);
	}

	public PuiServiceUpdateException(Exception cause) {
		super(cause);
	}

}

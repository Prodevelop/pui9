package es.prodevelop.pui9.exceptions;

public class PuiServiceExistsException extends PuiServiceException {

	private static final long serialVersionUID = 1L;

	public PuiServiceExistsException(AbstractPuiDaoException cause) {
		super(cause);
	}

}

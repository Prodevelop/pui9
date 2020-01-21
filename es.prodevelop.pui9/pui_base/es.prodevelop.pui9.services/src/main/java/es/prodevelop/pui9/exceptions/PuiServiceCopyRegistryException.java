package es.prodevelop.pui9.exceptions;

public class PuiServiceCopyRegistryException extends PuiServiceException {

	private static final long serialVersionUID = 1L;

	public PuiServiceCopyRegistryException() {
		super(null);
	}

	public PuiServiceCopyRegistryException(PuiServiceException cause) {
		super(cause, cause.getMessage());
	}

	public PuiServiceCopyRegistryException(AbstractPuiDaoException cause) {
		super(cause);
	}

	public PuiServiceCopyRegistryException(Exception cause) {
		super(cause);
	}

}

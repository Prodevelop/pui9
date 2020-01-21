package es.prodevelop.pui9.exceptions;

public class PuiServiceException extends PuiException {

	private static final long serialVersionUID = 1L;

	/**
	 * Creates an exception with a parent exception
	 */
	public PuiServiceException(Exception cause) {
		super(cause);
	}

	/**
	 * Creates an exception with a coded message
	 */
	public PuiServiceException(Integer code, String message) {
		this(code, message, new Object[0]);
	}

	public PuiServiceException(Exception cause, String message) {
		super(cause, message);
	}

	/**
	 * Create the Exception with the given message and code and parameters.
	 */
	public PuiServiceException(Integer code, String message, Object... parameters) {
		this(null, code, message, parameters);
	}

	/**
	 * Create the Exception with the given message and code and parameters.
	 */
	public PuiServiceException(Exception cause, Integer code, String message, Object... parameters) {
		super(cause, code, message, parameters);
	}

}

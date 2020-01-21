package es.prodevelop.pui9.exceptions;

import java.text.MessageFormat;
import java.time.Instant;

import org.apache.http.HttpStatus;

import es.prodevelop.pui9.utils.PuiDateUtil;

/**
 * Generic exception for PUI applications. Subclasses of this exceptions are
 * teated specially and may provide a unique CODE and status response for the
 * client requests
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class PuiException extends Exception {

	private static final long serialVersionUID = 1L;
	private static final Integer DEFAULT_INTERNAL_CODE = -1;

	private static Throwable lookForNonPuiException(Throwable cause) {
		Throwable e = cause;
		while (e instanceof PuiException) {
			e = e.getCause();
		}
		return e;
	}

	private int internalCode = DEFAULT_INTERNAL_CODE;
	private String className = "";
	private String methodName = "";
	private Instant datetime;

	/**
	 * Creates an Exception with the given message
	 * 
	 * @param message The message of the Exception
	 */
	public PuiException(String message) {
		this(null, message != null ? message : "");
	}

	/**
	 * Creates an Exception with the given root cause
	 * 
	 * @param cause The main cause of the Exception
	 */
	public PuiException(Throwable cause) {
		this(cause, cause != null ? cause.getMessage() : "");
	}

	/**
	 * Creates an Exception with the given root cause and the provided message
	 * (without parameters)
	 * 
	 * @param cause   The main cause of the Exception
	 * @param message The message of the Exception
	 */
	public PuiException(Throwable cause, String message) {
		this(cause, null, message != null ? message : cause != null ? cause.getMessage() : "");
	}

	/**
	 * Creates an Exception with the given root cause, the given code and provided
	 * message (with parameters)
	 * 
	 * @param cause        the main cause of the Exception
	 * @param internalCode The internal code of the Exception
	 * @param message      The message of the Exception
	 * @param parameters   The parametters of the message
	 */
	public PuiException(Throwable cause, Integer internalCode, String message, Object... parameters) {
		super(parameters != null && parameters.length > 0
				? MessageFormat.format(
						message != null && message.length() > 0 ? message : cause != null ? cause.getMessage() : null,
						parameters)
				: message != null && message.length() > 0 ? message : cause != null ? cause.getMessage() : null,
				lookForNonPuiException(cause));
		if (cause instanceof PuiException) {
			this.internalCode = ((PuiException) cause).getInternalCode();
		} else {
			this.internalCode = internalCode != null ? internalCode : DEFAULT_INTERNAL_CODE;
		}
		this.datetime = Instant.now();
		setStackData();
	}

	/**
	 * Retrieve the last class and method that fired the exception from the Stack
	 * trace
	 */
	private void setStackData() {
		for (StackTraceElement ste : getStackTrace()) {
			if (ste.getClassName().contains(Thread.class.getSimpleName())) {
				continue;
			}
			if (ste.getClassName().endsWith(Exception.class.getSimpleName())) {
				continue;
			}

			this.className = ste.getClassName();
			this.methodName = ste.getMethodName();
			break;
		}
	}

	/**
	 * Get the internal unique exception code
	 * 
	 * @return The unique exception internal code
	 */
	public int getInternalCode() {
		return internalCode;
	}

	/**
	 * Get the {@link HttpStatus} status code for this Exception
	 * 
	 * @return The {@link HttpStatus} status code
	 */
	protected int getStatusResponse() {
		return HttpStatus.SC_INTERNAL_SERVER_ERROR;
	}

	/**
	 * If this Exception should be logged or not
	 * 
	 * @return True if should be logged; false if not
	 */
	public boolean shouldLog() {
		return true;
	}

	/**
	 * Convert the PuiException into a {@link PuiExceptionDto} object (valid to send
	 * to the clients)
	 * 
	 * @return The Exception in dto format
	 */
	public PuiExceptionDto asDto() {
		PuiExceptionDto dto = new PuiExceptionDto();
		dto.setInternalCode(internalCode);
		dto.setStatusCode(getStatusResponse());
		dto.setMessage(getMessage());
		if (getCause() != null) {
			dto.setDetailedMessage(getCause().getMessage());
		}

		return dto;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("\n#####################################################");
		sb.append("\nDate: " + PuiDateUtil.temporalAccessorToString(datetime));
		sb.append("\nInternalCode: " + internalCode);
		sb.append("\nClass: " + className);
		sb.append("\nMethod: " + methodName);
		sb.append("\nMessage: " + getMessage());
		if (getCause() != null) {
			sb.append("\nOriginalMessage: " + getCause().getMessage());
		}
		sb.append("\n#####################################################\n");

		return sb.toString();
	}

}

package es.prodevelop.pui9.exceptions;

import es.prodevelop.pui9.utils.IPuiObject;

/**
 * This class is a representation of the Exception thrown by the application,
 * and will be sent to the client as response when an Exception is thrown in the
 * server
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class PuiExceptionDto implements IPuiObject {

	private static final long serialVersionUID = 1L;

	private int internalCode;
	private int statusCode;
	private String message;
	private String detailedMessage;

	public int getInternalCode() {
		return internalCode;
	}

	public void setInternalCode(int internalCode) {
		this.internalCode = internalCode;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getDetailedMessage() {
		return detailedMessage;
	}

	public void setDetailedMessage(String detailedMessage) {
		this.detailedMessage = detailedMessage;
	}

}

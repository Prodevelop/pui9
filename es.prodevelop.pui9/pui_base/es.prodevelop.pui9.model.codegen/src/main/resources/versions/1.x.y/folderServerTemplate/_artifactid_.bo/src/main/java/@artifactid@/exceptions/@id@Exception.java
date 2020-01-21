package _artifactid_.exceptions;

import _artifactid_.messages._id_Messages;
import es.prodevelop.pui9.exceptions.PuiServiceException;

public class _id_Exception extends PuiServiceException {

	private static final long serialVersionUID = 1L;

	public _id_Exception(Integer code) {
		this(code, new Object[0]);
	}

	public _id_Exception(Integer code, Object... parameters) {
		super(code, _id_Messages.getSingleton().getString(code), parameters);
	}

}

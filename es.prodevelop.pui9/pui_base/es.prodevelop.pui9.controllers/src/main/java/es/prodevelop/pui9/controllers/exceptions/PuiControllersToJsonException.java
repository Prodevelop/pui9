package es.prodevelop.pui9.controllers.exceptions;

public class PuiControllersToJsonException extends AbstractPuiControllersException {

	private static final long serialVersionUID = 1L;

	public static final Integer CODE = 802;

	public PuiControllersToJsonException(String error) {
		super(CODE, error);
	}

}

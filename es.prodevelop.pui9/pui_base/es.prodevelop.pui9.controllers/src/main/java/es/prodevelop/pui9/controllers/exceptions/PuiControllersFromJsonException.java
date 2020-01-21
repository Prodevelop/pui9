package es.prodevelop.pui9.controllers.exceptions;

public class PuiControllersFromJsonException extends AbstractPuiControllersException {

	private static final long serialVersionUID = 1L;

	public static final Integer CODE = 801;

	public PuiControllersFromJsonException(String error) {
		super(CODE, error);
	}

}

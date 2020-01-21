package es.prodevelop.pui9.geo.exceptions;

public class PuiGeoInitGeoServerException extends AbstractPuiGeoException {

	private static final long serialVersionUID = 1L;

	public static final Integer CODE = 602;

	public PuiGeoInitGeoServerException(Exception cause) {
		super(cause, CODE);
	}

}

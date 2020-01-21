package es.prodevelop.pui9.geo.exceptions;

public class PuiGeoCoordinatesException extends AbstractPuiGeoException {

	private static final long serialVersionUID = 1L;

	public static final Integer CODE = 601;

	public PuiGeoCoordinatesException(Exception cause) {
		super(cause, CODE);
	}

}

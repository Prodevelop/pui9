package es.prodevelop.pui9.exceptions;

/**
 * Unexpected exception when executing any operation over the database
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class PuiDaoDataAccessException extends AbstractPuiDaoException {

	private static final long serialVersionUID = 1L;

	public static final Integer CODE = 103;

	public PuiDaoDataAccessException(Exception cause) {
		super(cause, CODE);
	}

}

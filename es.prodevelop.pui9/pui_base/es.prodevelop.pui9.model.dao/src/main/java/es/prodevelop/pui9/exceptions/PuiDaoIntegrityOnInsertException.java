package es.prodevelop.pui9.exceptions;

/**
 * DAO exception that represents an integrity reference error when executing the
 * Insert operation over the database
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class PuiDaoIntegrityOnInsertException extends PuiDaoInsertException {

	private static final long serialVersionUID = 1L;

	public static final Integer CODE = 107;

	public PuiDaoIntegrityOnInsertException(Exception cause) {
		super(cause, CODE);
	}

}

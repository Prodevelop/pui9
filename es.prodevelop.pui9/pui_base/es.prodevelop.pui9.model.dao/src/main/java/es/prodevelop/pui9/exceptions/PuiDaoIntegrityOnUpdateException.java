package es.prodevelop.pui9.exceptions;

/**
 * DAO exception that represents an integrity reference error when executing the
 * Update operation over the database
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class PuiDaoIntegrityOnUpdateException extends PuiDaoUpdateException {

	private static final long serialVersionUID = 1L;

	public static final Integer CODE = 108;

	public PuiDaoIntegrityOnUpdateException(Exception cause) {
		super(cause, CODE);
	}

}

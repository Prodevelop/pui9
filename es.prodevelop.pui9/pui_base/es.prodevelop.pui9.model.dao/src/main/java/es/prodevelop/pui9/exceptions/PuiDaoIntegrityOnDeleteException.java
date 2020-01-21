package es.prodevelop.pui9.exceptions;

/**
 * DAO exception that represents an integrity reference error when executing the
 * Delete operation over the database
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class PuiDaoIntegrityOnDeleteException extends PuiDaoDeleteException {

	private static final long serialVersionUID = 1L;

	public static final Integer CODE = 106;

	public PuiDaoIntegrityOnDeleteException(Exception cause) {
		super(cause, CODE);
	}

}

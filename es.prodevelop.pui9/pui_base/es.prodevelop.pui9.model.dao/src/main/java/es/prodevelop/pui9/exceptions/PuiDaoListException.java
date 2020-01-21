package es.prodevelop.pui9.exceptions;

/**
 * DAO exception when executing a List operation over the database
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class PuiDaoListException extends AbstractPuiDaoException {

	private static final long serialVersionUID = 1L;

	public static final Integer CODE = 109;

	public PuiDaoListException(Exception cause) {
		super(cause, CODE);
	}

}

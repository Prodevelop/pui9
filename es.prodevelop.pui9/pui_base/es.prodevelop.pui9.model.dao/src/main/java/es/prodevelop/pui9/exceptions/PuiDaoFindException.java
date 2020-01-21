package es.prodevelop.pui9.exceptions;

/**
 * DAO exception when executing a Find operation over the database
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class PuiDaoFindException extends AbstractPuiDaoException {

	private static final long serialVersionUID = 1L;

	public static final Integer CODE = 105;

	public PuiDaoFindException(Exception cause) {
		super(cause, CODE);
	}

}

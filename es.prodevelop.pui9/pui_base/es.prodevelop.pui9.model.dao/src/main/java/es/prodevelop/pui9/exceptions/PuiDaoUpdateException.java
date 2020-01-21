package es.prodevelop.pui9.exceptions;

/**
 * DAO exception when executing the Update operation over the database
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class PuiDaoUpdateException extends PuiDaoSaveException {

	private static final long serialVersionUID = 1L;

	public PuiDaoUpdateException(AbstractPuiDaoException cause) {
		super(cause);
	}

	public PuiDaoUpdateException(Exception cause, int code) {
		super(cause, code);
	}

}

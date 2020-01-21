package es.prodevelop.pui9.exceptions;

/**
 * DAO exception when executing the Insert operation over the database
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class PuiDaoInsertException extends PuiDaoSaveException {

	private static final long serialVersionUID = 1L;

	public PuiDaoInsertException(AbstractPuiDaoException cause) {
		super(cause);
	}

	public PuiDaoInsertException(Exception cause, int code) {
		super(cause, code);
	}

}

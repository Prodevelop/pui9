package es.prodevelop.pui9.exceptions;

/**
 * DAO exception when executing a Save operation (insert or delete) over the
 * database
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class PuiDaoSaveException extends AbstractPuiDaoException {

	private static final long serialVersionUID = 1L;

	public PuiDaoSaveException(AbstractPuiDaoException cause) {
		super(cause);
	}

	public PuiDaoSaveException(Exception cause, int code) {
		super(cause, code);
	}

}

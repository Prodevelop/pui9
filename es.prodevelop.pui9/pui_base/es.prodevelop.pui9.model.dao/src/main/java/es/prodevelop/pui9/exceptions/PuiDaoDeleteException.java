package es.prodevelop.pui9.exceptions;

/**
 * DAO exception when executing the Delete operation over the database
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class PuiDaoDeleteException extends AbstractPuiDaoException {

	private static final long serialVersionUID = 1L;

	public PuiDaoDeleteException(AbstractPuiDaoException cause) {
		super(cause.getInternalCode());
	}

	public PuiDaoDeleteException(Exception cause, int code) {
		super(cause, code);
	}

}

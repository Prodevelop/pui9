package es.prodevelop.pui9.exceptions;

/**
 * DAO exception when executing the Count operation over the database
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class PuiDaoNoNumericColumnException extends AbstractPuiDaoException {

	private static final long serialVersionUID = 1L;

	public static final Integer CODE = 111;

	public PuiDaoNoNumericColumnException(String columnName) {
		super(CODE, columnName);
	}

}

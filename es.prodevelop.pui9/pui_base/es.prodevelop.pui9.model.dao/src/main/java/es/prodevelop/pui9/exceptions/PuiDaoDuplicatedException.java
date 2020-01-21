package es.prodevelop.pui9.exceptions;

/**
 * DAO exception when executing an Insert or Update operation over the database
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class PuiDaoDuplicatedException extends AbstractPuiDaoException {

	private static final long serialVersionUID = 1L;

	public static final Integer CODE = 104;

	public PuiDaoDuplicatedException() {
		super(CODE);
	}

}

package es.prodevelop.pui9.exceptions;

import org.apache.http.HttpStatus;

/**
 * DAO exception when executing a Find operation over the database
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class PuiDaoNotExistsException extends AbstractPuiDaoException {

	private static final long serialVersionUID = 1L;

	public static final Integer CODE = 112;

	public PuiDaoNotExistsException() {
		super(CODE);
	}
	
	@Override
	protected int getStatusResponse() {
		return HttpStatus.SC_NOT_FOUND;
	}

}

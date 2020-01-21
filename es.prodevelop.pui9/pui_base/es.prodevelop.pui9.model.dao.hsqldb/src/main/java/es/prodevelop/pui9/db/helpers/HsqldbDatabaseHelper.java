package es.prodevelop.pui9.db.helpers;

import org.springframework.stereotype.Component;

/**
 * Database Helper for Hsqldb. Concret implementation of Abstract Database
 * Helper
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
@Component
public class HsqldbDatabaseHelper extends AbstractDatabaseHelper {

	@Override
	protected String getSqlConvertNumberIntoString() {
		throw new UnsupportedOperationException();
	}

	@Override
	protected String getSqlConvertDateIntoString() {
		throw new UnsupportedOperationException();
	}

	@Override
	protected String getSqlConvertStringIntoDate() {
		throw new UnsupportedOperationException();
	}

	@Override
	protected String getSqlTextOperation(boolean isLargeStringField) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getSqlForPagination(int page, int size, String query) {
		throw new UnsupportedOperationException();
	}

}

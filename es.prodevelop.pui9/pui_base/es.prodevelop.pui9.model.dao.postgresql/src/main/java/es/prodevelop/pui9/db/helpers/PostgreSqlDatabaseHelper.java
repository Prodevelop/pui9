package es.prodevelop.pui9.db.helpers;

import org.springframework.stereotype.Component;

/**
 * Database Helper for PostgreSql. Concret implementation of Abstract Database
 * Helper
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
@Component
public class PostgreSqlDatabaseHelper extends AbstractDatabaseHelper {

	private static final String DATE_TIME_FORMAT = DATE_FORMAT + "\"T\"HH24:MI:SS\"Z\"";
	private static final String POSTGRESQL_DATE_TIME_FORMAT = "YYYY-MM-DD\"T\"HH24:MI:SS.MS\"Z\"";

	@Override
	protected String getSqlConvertNumberIntoString() {
		return "CAST(" + COLUMNNAME + " AS VARCHAR)";
	}

	protected String getSqlConvertDateIntoString() {
		return "TO_CHAR(" + COLUMNNAME + ", '" + adaptDateFormatToUser(DATE_TIME_FORMAT) + "')";
	}

	protected String getSqlConvertStringIntoDate() {
		return "TO_TIMESTAMP('" + VALUE + "', '" + POSTGRESQL_DATE_TIME_FORMAT + "')";
	}

	@Override
	protected String getSqlTextOperation(boolean isLargeStringField) {
		return "UNACCENT(LOWER(" + COLUMNNAME + ")) " + OP + " '" + BEGINNING + "' || UNACCENT(LOWER('" + VALUE
				+ "')) || '" + END + "'";
	}

	@Override
	public String getSqlForPagination(int page, int size, String query) {
		StringBuilder sb = new StringBuilder();

		int from = page * size;

		sb.append(query + " \n");
		sb.append("limit " + size + " offset " + from);

		return sb.toString();
	}

}

package es.prodevelop.pui9.db.helpers;

import org.springframework.stereotype.Component;

/**
 * Database Helper for Oracle. Concret implementation of Abstract Database
 * Helper
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
@Component
public class OracleDatabaseHelper extends AbstractDatabaseHelper {

	private static final String DATE_TIME_FORMAT = DATE_FORMAT + "\"T\"HH24:MI:SS\"Z\"";
	private static final String ORACLE_DATE_TIME_FORMAT = "YYYY-MM-DD\"T\"HH24:MI:SS.FF3\"Z\"";

	@Override
	protected String getSqlConvertNumberIntoString() {
		return "TO_CHAR(" + COLUMNNAME + ")";
	}

	@Override
	protected String getSqlConvertDateIntoString() {
		return "TO_CHAR(" + COLUMNNAME + ", '" + adaptDateFormatToUser(DATE_TIME_FORMAT) + "')";
	}

	@Override
	protected String getSqlConvertStringIntoDate() {
		return "TO_TIMESTAMP('" + VALUE + "', '" + ORACLE_DATE_TIME_FORMAT + "')";
	}

	@Override
	protected String getSqlTextOperation(boolean isLargeStringField) {
		if (!isLargeStringField) {
			return "TRANSLATE(LOWER(" + COLUMNNAME + "), 'áéíóúèò', 'aeioueo') " + OP + " '" + BEGINNING
					+ "' || TRANSLATE(LOWER('" + VALUE + "'),'áéíóúèò','aeioueo') || '" + END + "'";
		} else {
			return "LOWER(" + COLUMNNAME + ") " + OP + " '" + BEGINNING + "' || TRANSLATE(LOWER('" + VALUE
					+ "'),'áéíóúèò','aeioueo') || '" + END + "'";
		}
	}

	@Override
	public String getSqlForPagination(int page, int size, String query) {
		StringBuilder sb = new StringBuilder();

		int from = page * size + 1;
		int to = from + size - 1;

		sb.append("select * \n");
		sb.append("from ( \n");
		sb.append("select rownum rnum, a.* \n");
		sb.append("from ( \n");
		sb.append(query + "\n");
		sb.append(") a \n");
		sb.append("where rownum <= " + to + " \n");
		sb.append(") \n");
		sb.append("where rnum >= " + from);

		return sb.toString();
	}

}

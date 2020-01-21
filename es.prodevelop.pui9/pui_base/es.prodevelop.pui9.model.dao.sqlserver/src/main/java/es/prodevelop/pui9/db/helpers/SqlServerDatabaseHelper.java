package es.prodevelop.pui9.db.helpers;

import org.springframework.stereotype.Component;

/**
 * Database Helper for SqlServer. Concret implementation of Abstract Database
 * Helper
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
@Component
public class SqlServerDatabaseHelper extends AbstractDatabaseHelper {

	private static final String DATE_TIME_FORMAT = DATE_FORMAT + "THH:mm:ssZ";

	@Override
	protected String getSqlConvertNumberIntoString() {
		return "CAST(" + COLUMNNAME + " AS VARCHAR)";
	}

	protected String getSqlConvertDateIntoString() {
		return "FORMAT(" + COLUMNNAME + ", '" + adaptDateFormatToUser(DATE_TIME_FORMAT) + "')";
	}

	protected String getSqlConvertStringIntoDate() {
		return "CONVERT(datetime, '" + VALUE + "', 127)";
	}

	@Override
	protected String getSqlTextOperation(boolean isLargeStringField) {
		return "LOWER(" + COLUMNNAME + ") collate SQL_Latin1_General_Cp1251_CS_AS " + OP + " '" + BEGINNING
				+ "' + LOWER('" + VALUE + "') + '" + END + "'";
	}

	@Override
	public String getSqlForPagination(int page, int size, String query) {
		StringBuilder sb = new StringBuilder();

		int from = page * size;

		sb.append(query + " \n");
		if (!query.toLowerCase().contains("order by")) {
			sb.append("order by 1 \n");
		}
		sb.append("offset " + from + " rows fetch next " + size + " rows only");

		return sb.toString();
	}

}

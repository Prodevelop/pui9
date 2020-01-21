package es.prodevelop.pui9.utils.csv;

import java.util.ArrayList;
import java.util.List;

public class CsvContent {

	private CsvHeader headers;

	private List<CsvRow> rows;

	private boolean includeBlankRowAtEnd;

	public CsvContent() {
		headers = new CsvHeader();
		rows = new ArrayList<>();
		includeBlankRowAtEnd = false;
	}

	public void addHeader(String header) {
		headers.addHeader(header);
	}

	public void addRow(CsvRow row) {
		rows.add(row);
	}

	public CsvHeader getHeaders() {
		return headers;
	}

	public List<CsvRow> getRows() {
		return rows;
	}

	/**
	 * By default, true
	 */
	public boolean includeBlankRowAtEnd() {
		return includeBlankRowAtEnd;
	}

	public void setIncludeBlankRowAtEnd(boolean includeBlankRowAtEnd) {
		this.includeBlankRowAtEnd = includeBlankRowAtEnd;
	}
}

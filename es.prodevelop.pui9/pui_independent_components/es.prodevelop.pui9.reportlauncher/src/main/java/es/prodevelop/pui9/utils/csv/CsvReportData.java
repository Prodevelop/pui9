package es.prodevelop.pui9.utils.csv;

import es.prodevelop.pui9.utils.AbstractReportData;

public class CsvReportData extends AbstractReportData {

	private CsvContent contents;

	public CsvReportData(String reportPath, String reportName, CsvContent contents) {
		super(reportPath, reportName);
		this.contents = contents;
	}

	public CsvContent getContents() {
		return contents;
	}

}

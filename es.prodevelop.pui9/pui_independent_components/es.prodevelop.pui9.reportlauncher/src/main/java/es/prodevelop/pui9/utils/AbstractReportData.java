package es.prodevelop.pui9.utils;

public class AbstractReportData implements IReportData {

	private String reportPath;
	private String reportName;

	public AbstractReportData(String reportPath, String reportName) {
		this.reportPath = reportPath;
		this.reportName = reportName;
	}

	public String getReportPath() {
		return reportPath;
	}

	public void setReportPath(String reportPath) {
		this.reportPath = reportPath;
	}

	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}
}

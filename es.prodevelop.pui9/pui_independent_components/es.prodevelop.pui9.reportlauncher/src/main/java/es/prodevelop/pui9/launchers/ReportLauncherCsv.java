package es.prodevelop.pui9.launchers;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.util.Collection;

import es.prodevelop.pui9.csv.CsvWriter;
import es.prodevelop.pui9.utils.csv.CsvReportData;
import es.prodevelop.pui9.utils.csv.CsvRow;

public class ReportLauncherCsv extends AbstractReportLauncher {

	private CsvReportData data;

	public ReportLauncherCsv(CsvReportData data) {
		this.data = data;
	}

	@Override
	public void launch(int reportElementSize) throws Exception {
		CsvWriter writer = getWriter();

		if (data.getContents().getHeaders() != null) {
			writer.writeRecord(data.getContents().getHeaders().getHeaders());
		}
		for (CsvRow row : data.getContents().getRows()) {
			writer.writeRecord(row.getCells());
		}
		if (data.getContents().includeBlankRowAtEnd()) {
			writer.writeRecord(new String[1]);
		}

		writer.close();
	}

	@Override
	public void launch(Connection connection) throws Exception {
		throw new UnsupportedOperationException("Not implemented method");
	}

	@Override
	public void launch(Collection<?> collection) throws Exception {
		throw new UnsupportedOperationException("Not implemented method");
	}

	private CsvWriter getWriter() {
		baos = new ByteArrayOutputStream();
		CsvWriter writer = new CsvWriter(baos, ';', Charset.forName("UTF-8"));
		return writer;
	}

	@Override
	public File getResultAsFile(String folder, boolean includeTimeMillis) throws Exception {
		String fileName = getFileName(folder, data.getReportName(), includeTimeMillis);

		File file = new File(fileName);
		FileOutputStream fos = new FileOutputStream(file);

		baos.writeTo(fos);
		return file;
	}
}

package es.prodevelop.pui9.launchers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;

import es.prodevelop.pui9.utils.pdf.PdfReportData;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;

public class ReportLauncherPdf extends AbstractReportLauncher {

	private PdfReportData data;

	public ReportLauncherPdf(PdfReportData data) {
		super();
		this.data = data;
	}

	public PdfReportData getData() {
		return data;
	}

	@Override
	public void launch(int reportElementSize) throws Exception {
		JasperPrint jasperPrint = getCompiledReport(reportElementSize);
		innerLaunch(jasperPrint);
	}

	@Override
	public void launch(Connection connection) throws Exception {
		JasperPrint jasperPrint = getCompiledReport(connection);
		innerLaunch(jasperPrint);
	}

	@Override
	public void launch(Collection<?> collection) throws Exception {
		JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(collection);
		JasperPrint jasperPrint = getCompiledReport(dataSource);
		innerLaunch(jasperPrint);
	}

	private void innerLaunch(JasperPrint jasperPrint) throws Exception {
		JRPdfExporter exp = new JRPdfExporter();
		exp.setExporterInput(new SimpleExporterInput(jasperPrint));
		exp.setExporterOutput(new SimpleOutputStreamExporterOutput(baos));
		exp.exportReport();
	}

	private JasperPrint getCompiledReport(JRDataSource dataSource) throws JRException {
		Map<String, Object> params = data.getParameters();
		params.put(JRParameter.REPORT_LOCALE, new Locale("es", "ES"));

		InputStream is = getClass().getClassLoader().getResourceAsStream(data.getReportPath());
		if (isCompiledFile(data.getReportPath())) {
			return JasperFillManager.fillReport(is, params, dataSource);
		} else {
			JasperReport report = JasperCompileManager.compileReport(is);
			return JasperFillManager.fillReport(report, params, dataSource);
		}
	}

	private JasperPrint getCompiledReport(int reportElementSize) throws JRException {
		Map<String, Object> params = data.getParameters();
		params.put(JRParameter.REPORT_LOCALE, new Locale("es", "ES"));

		InputStream is = getClass().getClassLoader().getResourceAsStream(data.getReportPath());
		if (isCompiledFile(data.getReportPath())) {
			return JasperFillManager.fillReport(is, params, new JREmptyDataSource(reportElementSize));
		} else {
			JasperReport report = JasperCompileManager.compileReport(is);
			return JasperFillManager.fillReport(report, params, new JREmptyDataSource(reportElementSize));
		}
	}

	private JasperPrint getCompiledReport(Connection connection) throws JRException {
		Map<String, Object> params = data.getParameters();
		params.put(JRParameter.REPORT_LOCALE, new Locale("es", "ES"));

		InputStream is = getClass().getClassLoader().getResourceAsStream(data.getReportPath());
		if (isCompiledFile(data.getReportPath())) {
			return JasperFillManager.fillReport(is, params, connection);
		} else {
			JasperReport report = JasperCompileManager.compileReport(is);
			return JasperFillManager.fillReport(report, params, connection);
		}
	}

	/**
	 * File with '.jasper' extension is a compiled report. Else, file has '.jrxml'
	 * extension
	 */
	private boolean isCompiledFile(String path) {
		return path.endsWith(".jasper");
	}

	@Override
	public File getResultAsFile(String folder, boolean includeTimeMillis) throws Exception {
		String fileName = getFileName(folder, data.getReportName(), includeTimeMillis);

		File file = new File(fileName);
		FileOutputStream fos = new FileOutputStream(file);

		ByteArrayOutputStream mybaos = mergePDFs(Collections.singletonList(baos));
		mybaos.writeTo(fos);
		mybaos.close();
		fos.close();

		return file;
	}

	/**
	 * Merges a list of pdfs output stream into a single output stream
	 */
	public static ByteArrayOutputStream mergePDFs(List<ByteArrayOutputStream> pdfs) throws Exception {
		PDFMergerUtility ut = new PDFMergerUtility();
		for (ByteArrayOutputStream baos : pdfs) {
			ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
			ut.addSource(bais);
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ut.setDestinationStream(baos);
		try {
			ut.mergeDocuments(MemoryUsageSetting.setupMainMemoryOnly());
		} catch (Exception e) {
		}
		return baos;
	}

}

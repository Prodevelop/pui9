package es.prodevelop.pui9.launchers;

import java.io.ByteArrayOutputStream;

public abstract class AbstractReportLauncher implements IReportLauncher {

	protected ByteArrayOutputStream baos;

	public static String PDF_FULL_FOLDER = "";
	public static final String PDF_FOLDER = "/reports/";
	static {
		String os = System.getProperty("os.name");
		if (os != null && os.toLowerCase().indexOf("win") >= 0) {
			PDF_FULL_FOLDER = "c:/www" + PDF_FOLDER;
		} else {
			PDF_FULL_FOLDER = "/var/www" + PDF_FOLDER;
		}
	}

	public AbstractReportLauncher() {
		this.baos = new ByteArrayOutputStream();
	}

	@Override
	public void launch() throws Exception {
		launch(1);
	}

	@Override
	public ByteArrayOutputStream getResultAsOutputStream() {
		return baos;
	}

	protected String getFileName(String folder, String baseName, boolean includeTimeMillis) {
		String fileName = baseName + (includeTimeMillis ? ("_" + System.currentTimeMillis()) : "") + ".pdf";

		String reportPath = (folder != null ? folder : PDF_FULL_FOLDER) + fileName;

		return reportPath;
	}

}

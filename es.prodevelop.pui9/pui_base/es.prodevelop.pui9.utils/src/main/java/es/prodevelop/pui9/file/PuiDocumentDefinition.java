package es.prodevelop.pui9.file;

import java.io.InputStream;

public class PuiDocumentDefinition {

	private InputStream inputStream;
	private String originalFileName;
	private String fileName;
	private String fileExtension;
	private long fileSize;

	private String uniqueFileName;

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public String getOriginalFileName() {
		return originalFileName;
	}

	public void setOriginalFileName(String originalFileName) {
		this.originalFileName = originalFileName;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileExtension() {
		return fileExtension;
	}

	public void setFileExtension(String fileExtension) {
		this.fileExtension = fileExtension;
	}

	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

	public String getFullFileName() {
		return getFileName() + "." + getFileExtension();
	}

	public String getUniqueFullFileName() {
		if (uniqueFileName == null) {
			uniqueFileName = getFileName() + "_" + System.currentTimeMillis() + "." + getFileExtension();
		}
		return uniqueFileName;
	}
}
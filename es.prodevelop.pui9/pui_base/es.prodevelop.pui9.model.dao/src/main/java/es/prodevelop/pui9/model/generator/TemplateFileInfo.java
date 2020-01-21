package es.prodevelop.pui9.model.generator;

import java.io.File;

/**
 * Helper class to hold all the necessary Template info
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class TemplateFileInfo {
	private String templateName;
	private String generatedFileName;
	private String packageName;
	private String fileName;
	private String contents;
	private File tempSrcFile;
	private File tempBinFolder;
	private File tempBinFile;
	private Boolean isJava = true;

	public TemplateFileInfo(String templateName, String generatedFileName, String packageName) {
		this.templateName = templateName;
		this.generatedFileName = generatedFileName;
		this.packageName = packageName;
	}

	public String getTemplateName() {
		return templateName;
	}

	public String getGeneratedFileName() {
		return generatedFileName;
	}

	public String getPackageName() {
		return packageName;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public File getTempSrcFile() {
		return tempSrcFile;
	}

	public void setTempSrcFile(File tempSrcFile) {
		this.tempSrcFile = tempSrcFile;
	}

	public File getTempBinFolder() {
		return tempBinFolder;
	}

	public void setTempBinFolder(File tempBinFolder) {
		this.tempBinFolder = tempBinFolder;
	}

	public File getTempBinFile() {
		return tempBinFile;
	}

	public void setTempBinFile(File tempBinFile) {
		this.tempBinFile = tempBinFile;
	}

	public Boolean getIsJava() {
		return isJava;
	}

	public void setIsJava(Boolean isJava) {
		this.isJava = isJava;
	}

	@Override
	public String toString() {
		return fileName;
	}

}
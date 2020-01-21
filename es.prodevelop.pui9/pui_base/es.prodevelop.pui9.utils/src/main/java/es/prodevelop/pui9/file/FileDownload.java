package es.prodevelop.pui9.file;

import java.io.File;
import java.io.InputStream;

import es.prodevelop.pui9.utils.IPuiObject;

/**
 * This is a helper class to specify that a Controller returns a File. You can
 * use an existing File or an input stream
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class FileDownload implements IPuiObject {

	private static final long serialVersionUID = 1L;

	private File file;
	private InputStream inputStream;
	private String filename;
	private boolean downloadable = true;

	public FileDownload(File file, String filename) {
		this(file, filename, true);
	}

	public FileDownload(File file, String filename, boolean downloadable) {
		this.file = file;
		this.filename = filename;
		this.downloadable = downloadable;
	}

	public FileDownload(InputStream inputStream, String filename) {
		this(inputStream, filename, true);
	}

	public FileDownload(InputStream inputStream, String filename, boolean downloadable) {
		this.inputStream = inputStream;
		this.filename = filename;
		this.downloadable = downloadable;
	}

	public File getFile() {
		return file;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public String getFilename() {
		return filename;
	}

	public boolean isDownloadable() {
		return downloadable;
	}

}

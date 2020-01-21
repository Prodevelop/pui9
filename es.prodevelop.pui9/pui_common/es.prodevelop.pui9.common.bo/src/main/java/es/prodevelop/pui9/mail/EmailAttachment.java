package es.prodevelop.pui9.mail;

import java.io.Serializable;

/**
 * This class represents an attachment for the email
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class EmailAttachment implements Serializable {

	private static final long serialVersionUID = 1L;

	private String filename;
	private String contentType;
	private byte[] content;

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

}

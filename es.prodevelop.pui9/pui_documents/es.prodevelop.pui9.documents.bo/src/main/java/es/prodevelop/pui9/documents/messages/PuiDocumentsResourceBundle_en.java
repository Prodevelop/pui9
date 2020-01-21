package es.prodevelop.pui9.documents.messages;

/**
 * English Translation for PUI Documents component messages
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class PuiDocumentsResourceBundle_en extends PuiDocumentsResourceBundle {

	@Override
	protected String getExtensionsMessage_401() {
		return "File extension not allowed";
	}

	@Override
	protected String getFileSizeMessage_402() {
		return "File size exceed the maximum allowed";
	}

	@Override
	protected String getThumbnailMessage_403() {
		return "An error occurred while generating the thumbnails of the image. Please check the file and try again";
	}

	@Override
	protected String getUploadMessage_404() {
		return "Error while saving the document. Please, check the document";
	}
	
	@Override
	protected String getUpdateMessage_405() {
		return "Only the description, role and language of the document can be modified";
	}

}

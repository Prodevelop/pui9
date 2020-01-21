package es.prodevelop.pui9.documents.messages;

/**
 * Spanish Translation for PUI Documents component messages
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class PuiDocumentsResourceBundle_es extends PuiDocumentsResourceBundle {

	@Override
	protected String getExtensionsMessage_401() {
		return "Extensión de fichero no permitida";
	}

	@Override
	protected String getFileSizeMessage_402() {
		return "El tamaño del archivo supera el máximo permitido";
	}

	@Override
	protected String getThumbnailMessage_403() {
		return "Ha habido un error al generar las miniaturas de la imagen. Compruebe que el fichero no está corrupto";
	}

	@Override
	protected String getUploadMessage_404() {
		return "Error al guardar el documento. Revise el documento";
	}
	
	@Override
	protected String getUpdateMessage_405() {
		return "Solamente se puede actualizar la descripción, el rol y el idioma del documento";
	}

}

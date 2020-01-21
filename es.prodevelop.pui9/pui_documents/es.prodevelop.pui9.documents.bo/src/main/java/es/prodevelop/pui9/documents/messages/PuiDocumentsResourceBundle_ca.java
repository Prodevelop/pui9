package es.prodevelop.pui9.documents.messages;

/**
 * Catalan Translation for PUI Documents component messages
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class PuiDocumentsResourceBundle_ca extends PuiDocumentsResourceBundle {

	@Override
	protected String getExtensionsMessage_401() {
		return "Extensió de fitxer no permesa";
	}

	@Override
	protected String getFileSizeMessage_402() {
		return "La grandària del fitxer supera el màxim permés";
	}

	@Override
	protected String getThumbnailMessage_403() {
		return "Ha hagut un error generant les miniatures de la imatge. Comprove que el fitxer no està corrupte";
	}

	@Override
	protected String getUploadMessage_404() {
		return "Error al guardar el document. Revise el document";
	}

	@Override
	protected String getUpdateMessage_405() {
		return "Sols es pot actualitzar la descripció, el rol i l'idioma del document";
	}

}

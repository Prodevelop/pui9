package es.prodevelop.pui9.documents.messages;

/**
 * French Translation for PUI Documents component messages
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class PuiDocumentsResourceBundle_fr extends PuiDocumentsResourceBundle {

	@Override
	protected String getExtensionsMessage_401() {
		return "Extension de fichier non autorisée";
	}

	@Override
	protected String getFileSizeMessage_402() {
		return "La taille du fichier dépasse le maximum autorisé";
	}

	@Override
	protected String getThumbnailMessage_403() {
		return "Une erreur est survenue lors de la génération de l'image miniature. Vérifiez que le fichier n'est pas corrompu";
	}

	@Override
	protected String getUploadMessage_404() {
		return "Erreur d'enregistrement du document. Vérifiez le document";
	}
	
	@Override
	protected String getUpdateMessage_405() {
		return "Seule la description, le rôle et la langue du document peuvent être modifiés";
	}

}

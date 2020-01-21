 package es.prodevelop.pui9.docgen.messages;

/**
 * French Translation for PUI Docgen component messages
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class PuiDocgenResourceBundle_fr extends PuiDocgenResourceBundle {

	@Override
	protected String getNoElementsMessage_501() {
		return "Aucun élément correspondant avec le filtre du modèle afin de pouvoir le générer";
	}

	@Override
	protected String getNoParserMessage_502() {
		return "Il n''y a pas un analyseur de texte pour le type de fichier du modèle ''{0}''";
	}

	@Override
	protected String getUploadingTemplateMessage_503() {
		return "Erreur d'enregistrement du modèle. Vérifiez le document";
	}

	@Override
	protected String getModelNotExistsMessage_504() {
		return "N''existe pas le modèle ''{0}'' associé au modèle";
	}
	
	@Override
	protected String getGenerateMessage_505() {
		return "Une erreur est survenue lors de la génération du document: {0}";
	}

}

package es.prodevelop.pui9.docgen.messages;

/**
 * Catalan Translation for PUI Docgen component messages
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class PuiDocgenResourceBundle_ca extends PuiDocgenResourceBundle {

	@Override
	protected String getNoElementsMessage_501() {
		return "No hi ha elements que coincidisquen amb el filtre de la plantilla per a poder-la generar";
	}

	@Override
	protected String getNoParserMessage_502() {
		return "No existeix un analitzador de text per al tipus de fitxer de la plantilla ''{0}''";
	}

	@Override
	protected String getUploadingTemplateMessage_503() {
		return "Error al guardar la plantilla. Revise el document";
	}

	@Override
	protected String getModelNotExistsMessage_504() {
		return "No existeix el model ''{0}'' associat a la plantilla";
	}
	
	@Override
	protected String getGenerateMessage_505() {
		return "S''ha produït un error al generar el document: {0}";
	}

}

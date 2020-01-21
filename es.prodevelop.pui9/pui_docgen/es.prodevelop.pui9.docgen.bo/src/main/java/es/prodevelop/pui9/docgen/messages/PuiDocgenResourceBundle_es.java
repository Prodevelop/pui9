package es.prodevelop.pui9.docgen.messages;

/**
 * Spanish Translation for PUI Docgen component messages
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class PuiDocgenResourceBundle_es extends PuiDocgenResourceBundle {

	@Override
	protected String getNoElementsMessage_501() {
		return "No hay elementos que coincidan con el filtro de la plantilla para poderla generar";
	}

	@Override
	protected String getNoParserMessage_502() {
		return "No existe un analizador de texto para el tipo de fichero de la plantilla ''{0}''";
	}

	@Override
	protected String getUploadingTemplateMessage_503() {
		return "Error al guardar la plantilla. Revise la plantilla";
	}

	@Override
	protected String getModelNotExistsMessage_504() {
		return "No existe el modelo ''{0}'' asociado a la plantilla";
	}
	
	@Override
	protected String getGenerateMessage_505() {
		return "Se ha producido un error al generar el documento: {0}";
	}

}

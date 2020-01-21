package es.prodevelop.pui9.docgen.messages;

/**
 * English Translation for PUI Docgen component messages
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class PuiDocgenResourceBundle_en extends PuiDocgenResourceBundle {

	@Override
	protected String getNoElementsMessage_501() {
		return "Don't exist elements that match with the filter of the template to generate it";
	}

	@Override
	protected String getNoParserMessage_502() {
		return "Doesn''t exist a parser for the given template file type ''{0}''";
	}

	@Override
	protected String getUploadingTemplateMessage_503() {
		return "Error while saving the template. Please, check the template";
	}

	@Override
	protected String getModelNotExistsMessage_504() {
		return "Doesn''t exist the model ''{0}'' associated to the template";
	}
	
	@Override
	protected String getGenerateMessage_505() {
		return "Error while generating the document: {0}";
	}

}

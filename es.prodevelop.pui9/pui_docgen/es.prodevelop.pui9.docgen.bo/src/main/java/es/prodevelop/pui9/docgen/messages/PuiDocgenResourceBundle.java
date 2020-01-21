package es.prodevelop.pui9.docgen.messages;

import java.util.HashMap;
import java.util.Map;

import es.prodevelop.pui9.docgen.exceptions.PuiDocgenGenerateException;
import es.prodevelop.pui9.docgen.exceptions.PuiDocgenNoElementsException;
import es.prodevelop.pui9.docgen.exceptions.PuiDocgenNoParserException;
import es.prodevelop.pui9.docgen.exceptions.PuiDocgenUploadingTemplateException;
import es.prodevelop.pui9.docgen.exceptions.PuiDocgenModelNotExistsException;
import es.prodevelop.pui9.messages.AbstractPuiListResourceBundle;

/**
 * More specific implementation of {@link AbstractPuiListResourceBundle} for PUI
 * Docgen component
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public abstract class PuiDocgenResourceBundle extends AbstractPuiListResourceBundle {

	@Override
	protected Map<Object, String> getMessages() {
		Map<Object, String> messages = new HashMap<>();
		messages.put(PuiDocgenNoElementsException.CODE, getNoElementsMessage_501());
		messages.put(PuiDocgenNoParserException.CODE, getNoParserMessage_502());
		messages.put(PuiDocgenUploadingTemplateException.CODE, getUploadingTemplateMessage_503());
		messages.put(PuiDocgenModelNotExistsException.CODE, getModelNotExistsMessage_504());
		messages.put(PuiDocgenGenerateException.CODE, getGenerateMessage_505());

		return messages;
	}

	protected abstract String getNoElementsMessage_501();

	protected abstract String getNoParserMessage_502();

	protected abstract String getUploadingTemplateMessage_503();

	protected abstract String getModelNotExistsMessage_504();

	protected abstract String getGenerateMessage_505();

}

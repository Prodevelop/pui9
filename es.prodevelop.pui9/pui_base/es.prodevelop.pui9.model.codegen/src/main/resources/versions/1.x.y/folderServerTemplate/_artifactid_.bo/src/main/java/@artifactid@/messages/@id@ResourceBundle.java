package _artifactid_.messages;

import java.util.HashMap;
import java.util.Map;

import es.prodevelop.pui9.messages.AbstractPuiListResourceBundle;

public abstract class _id_ResourceBundle extends AbstractPuiListResourceBundle {

	public static final String sampleMessage = "sampleMessage";

	@Override
	protected Map<Object, String> getMessages() {
		Map<Object, String> messages = new HashMap<>();

		// messages
		messages.put(sampleMessage, getSampleMessage());

		// exceptions
//		messages.put(MyApplicationException.CODE, getMyApplication_1000());

		return messages;
	}

	protected abstract String getSampleMessage();

//	protected abstract String getMyApplication_1000();

}

package es.prodevelop.pui9.messages;

import java.text.MessageFormat;

import es.prodevelop.pui9.utils.PuiLanguage;
import es.prodevelop.pui9.utils.PuiLanguageUtils;

/**
 * Utility class to get internationalized messages for all PUI framework and the
 * Applications that use PUI
 */
public abstract class AbstractPuiMessages {

	public static final String GET_SINGLETON_METHOD_NAME = "getSingleton";

	private String baseName;

	protected AbstractPuiMessages() {
		baseName = getResourceBundleClass().getName();
		PuiMessagesRegistry.getSingleton().registerMessages(baseName);
	}

	protected abstract Class<?> getResourceBundleClass();

	public String getString(Integer key) {
		return getString(key != null ? key.toString() : null);
	}

	public String getString(String key, Object... params) {
		return getString(key, PuiLanguageUtils.getSessionLanguage(), params);
	}

	public String getString(String key, PuiLanguage language, Object... params) {
		String message = PuiMessagesRegistry.getSingleton().getString(baseName, language, key);
		if (message == null) {
			message = PuiMessagesRegistry.getSingleton().getString(language, key);
		}
		if (params != null && params.length > 0) {
			message = MessageFormat.format(message, params);
		}
		return message;
	}

}

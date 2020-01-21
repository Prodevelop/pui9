package es.prodevelop.pui9.elasticsearch.messages;

import es.prodevelop.pui9.messages.PuiMessagesRegistry;

/**
 * Utility class to get internationalized messages for PUI ElasticSearch
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class PuiElasticSearchMessages {

	private static PuiElasticSearchMessages singleton;

	public static PuiElasticSearchMessages getSingleton() {
		if (singleton == null) {
			singleton = new PuiElasticSearchMessages();
		}
		return singleton;
	}

	private String baseName;

	private PuiElasticSearchMessages() {
		baseName = getResourceBundleClass().getName();
		PuiMessagesRegistry.getSingleton().registerMessages(baseName);
	}

	private Class<?> getResourceBundleClass() {
		return PuiElasticsearchResourceBundle.class;
	}

	public String getString(Integer key) {
		return getString(key != null ? key.toString() : null);
	}

	private String getString(String key) {
		return PuiMessagesRegistry.getSingleton().getString(baseName, key);
	}

}

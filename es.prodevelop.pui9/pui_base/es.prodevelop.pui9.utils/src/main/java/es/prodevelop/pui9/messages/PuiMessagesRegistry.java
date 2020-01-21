package es.prodevelop.pui9.messages;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import es.prodevelop.pui9.utils.PuiLanguage;

/**
 * A Registry class to contain all the internationalization strings of the
 * server. All the "Message" classes should use this Registry to register its
 * messages and use them later
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class PuiMessagesRegistry {

	private static PuiMessagesRegistry singleton;

	public static PuiMessagesRegistry getSingleton() {
		if (singleton == null) {
			singleton = new PuiMessagesRegistry();
		}
		return singleton;
	}

	private final Map<String, Map<String, ResourceBundle>> map = new HashMap<>();
	private String[] AVAILABLE_LANGUAGES = { "es", "en", "ca", "fr" };
	private PuiLanguage DEFAULT_LANGUAGE = new PuiLanguage(AVAILABLE_LANGUAGES[0]);

	private PuiMessagesRegistry() {
	}

	/**
	 * Set the available languages of the application
	 * 
	 * @param langs The available languages
	 */
	public void setAvailableLanguages(String... langs) {
		AVAILABLE_LANGUAGES = langs;

		// remove the languages that will not available
		for (Iterator<String> it = map.keySet().iterator(); it.hasNext();) {
			if (!Arrays.asList(AVAILABLE_LANGUAGES).contains(it.next())) {
				it.remove();
			}
		}

		// add the new available languages
		for (String lang : AVAILABLE_LANGUAGES) {
			if (!map.containsKey(lang)) {
				map.put(lang, new HashMap<>());
				Map<String, ResourceBundle> oneLang = map.values().iterator().next();
				for (String baseMap : oneLang.keySet()) {
					addFileMessages(lang, baseMap);
				}
			}
		}
	}

	/**
	 * Set the default language of the application
	 * 
	 * @param lang The default language
	 */
	public void setDefaultLanguage(String lang) {
		DEFAULT_LANGUAGE = new PuiLanguage(lang);
	}

	/**
	 * Register a new file of properties
	 * 
	 * @param baseName The base name to search the messages
	 */
	public void registerMessages(String baseName) {
		if (baseName == null) {
			throw new IllegalArgumentException("baseName parameter cannot be null");
		}
		for (String lang : AVAILABLE_LANGUAGES) {
			addFileMessages(lang, baseName);
		}
	}

	/**
	 * Add a new file of properties for the given language
	 * 
	 * @param lang     The language of the file to be added
	 * @param baseName The base name of the filt to be added
	 */
	private void addFileMessages(String lang, String baseName) {
		try {
			if (!map.containsKey(lang)) {
				map.put(lang, new HashMap<>());
			}
			map.get(lang).put(baseName, ResourceBundle.getBundle(baseName, new Locale(lang)));
		} catch (MissingResourceException e) {
			map.get(lang).put(baseName, ResourceBundle.getBundle(baseName, new Locale(DEFAULT_LANGUAGE.getIsocode())));
		}
	}

	/**
	 * Get the message with the given key from the given base name file
	 * 
	 * @param baseName The base Name of the namespace to search for the key
	 * @param key      The key of the message to be searched
	 * @return The message
	 */
	public String getString(String baseName, String key) {
		return getString(baseName, DEFAULT_LANGUAGE, key);
	}

	/**
	 * Get the message with the given key from the given base name file with the
	 * selected language
	 * 
	 * @param baseName The base Name of the namespace to search for the key
	 * @param lang     The desired language for the message
	 * @param key      The key of the message to be searched
	 * @return The message
	 */
	public String getString(String baseName, PuiLanguage lang, String key) {
		if (baseName == null) {
			throw new IllegalArgumentException("baseName parameter cannot be null");
		}

		if (key == null) {
			return "";
		}

		if (lang == null || !map.containsKey(lang.getIsocode())) {
			lang = DEFAULT_LANGUAGE;
		}

		try {
			if (map.get(lang.getIsocode()).containsKey(baseName)) {
				return map.get(lang.getIsocode()).get(baseName).getString(key);
			} else {
				return null;
			}
		} catch (MissingResourceException e) {
			return null;
		}
	}

	/**
	 * Get the message of the given key for the given Language
	 * 
	 * @param lang The required language
	 * @param key  The key of the message
	 * @return The message
	 */
	public String getString(PuiLanguage lang, String key) {
		if (key == null) {
			return "";
		}

		if (lang == null || !map.containsKey(lang.getIsocode())) {
			lang = DEFAULT_LANGUAGE;
		}

		String val = null;
		for (ResourceBundle rb : map.get(lang.getIsocode()).values()) {
			try {
				val = rb.getString(key);
				break;
			} catch (MissingResourceException e) {
				// do nothing, continue with following resource bundle
			}
		}

		return val;
	}

}

package es.prodevelop.pui9.elasticsearch.enums;

/**
 * All the available languages for ElasticSearch, to use the correct language
 * analyzer with String types
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public enum ESLanguages {
	/**
	 * basque
	 */
	basque("eu"),

	/**
	 * brazilian
	 */
	brazilian("br"),

	/**
	 * catalan
	 */
	catalan("ca"),

	/**
	 * generic english
	 */
	english("en"),

	/**
	 * french
	 */
	french("fr"),

	/**
	 * galician
	 */
	galician("gl"),

	/**
	 * german
	 */
	getman("de"),

	/**
	 * italian
	 */
	italian("it"),

	/**
	 * portuguese
	 */
	portuguese("pt"),

	/**
	 * spanish
	 */
	spanish("es"),

	/**
	 * standard
	 */
	standard("st");

	public String code;

	private ESLanguages(String code) {
		this.code = code;
	}

	public static ESLanguages getByCode(String code) {
		if (code == null) {
			return null;
		}

		for (ESLanguages e : values()) {
			if (e.code.equals(code)) {
				return e;
			}
		}

		return null;
	}

}

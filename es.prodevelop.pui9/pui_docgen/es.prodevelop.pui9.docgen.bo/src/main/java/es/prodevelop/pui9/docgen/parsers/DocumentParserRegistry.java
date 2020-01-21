package es.prodevelop.pui9.docgen.parsers;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;

/**
 * A Registry of Document Parsers to manage the templates
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class DocumentParserRegistry {

	private static DocumentParserRegistry singleton;

	public static DocumentParserRegistry getSingleton() {
		if (singleton == null) {
			singleton = new DocumentParserRegistry();
		}
		return singleton;
	}

	private Map<String, IDocumentParser> map;

	private DocumentParserRegistry() {
		map = new HashMap<>();
	}

	public void registerDocumentParser(IDocumentParser parser) {
		if (map.containsKey(parser.getFileExtension())) {
			throw new IllegalArgumentException(
					"There exists more than one parser for '" + parser.getFileExtension() + "' document extension");
		}
		map.put(parser.getFileExtension(), parser);
	}

	public IDocumentParser getDocumentParser(String fileName) {
		String fileExtension = FilenameUtils.getExtension(fileName);
		if (fileExtension == null) {
			return null;
		}

		return map.get(fileExtension);
	}

}

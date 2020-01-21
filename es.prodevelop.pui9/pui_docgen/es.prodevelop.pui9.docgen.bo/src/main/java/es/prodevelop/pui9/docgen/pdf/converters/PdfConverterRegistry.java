package es.prodevelop.pui9.docgen.pdf.converters;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.springframework.util.StringUtils;

/**
 * A Registry of Document Parsers to manage the templates
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class PdfConverterRegistry {

	private static PdfConverterRegistry singleton;

	public static PdfConverterRegistry getSingleton() {
		if (singleton == null) {
			singleton = new PdfConverterRegistry();
		}
		return singleton;
	}

	private Map<String, IPdfConverter> map;

	private PdfConverterRegistry() {
		map = new HashMap<>();
	}

	public void registerPdfConverter(IPdfConverter converter) {
		if (map.containsKey(converter.getFileExtension())) {
			throw new IllegalArgumentException("There exists more than one converter for '"
					+ converter.getFileExtension() + "' document extension");
		}
		map.put(converter.getFileExtension(), converter);
	}

	public boolean existsPdfConverterForExtension(String fileExtension) {
		return map.containsKey(fileExtension);
	}

	public IPdfConverter getPdfConverter(String fileName) {
		String fileExtension = FilenameUtils.getExtension(fileName);
		if (StringUtils.isEmpty(fileExtension)) {
			fileExtension = fileName;
		}

		return map.get(fileExtension);
	}

}

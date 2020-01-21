package es.prodevelop.pui9.docgen.parsers;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import es.prodevelop.pui9.docgen.dto.MappingDto;
import es.prodevelop.pui9.file.FileDownload;
import es.prodevelop.pui9.model.dto.interfaces.IDto;

/**
 * Interface for representing Document Template Parsers
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public interface IDocumentParser {

	String TAG_PREFIX = "[{][$]";
	String TAG_SUFFIX = "[$][}]";
	String TAG_REGEX = TAG_PREFIX + "([^{]*)" + TAG_SUFFIX;

	String ROW_INI = "[<][$]" + "ROW_INI" + "[$][>]";
	String ROW_END = "[<][$]" + "ROW_END" + "[$][>]";

	/**
	 * Get the File Extension that this Parser supports
	 */
	String getFileExtension();

	/**
	 * Get the list of found tags in the given File (represented by the InputStream)
	 */
	List<String> getTags(InputStream inputStream);

	/**
	 * Parse the given template file using the list of given DTOs. The list of PK
	 * fields that represents the DTOs should be given when the template has
	 * details. Also the list of tag mapping should be provided
	 */
	FileDownload parse(File file, List<IDto> list, List<String> pkFields, List<MappingDto> mapping,
			List<String> columnsFilename, boolean isGeneratePdf) throws Exception;

}

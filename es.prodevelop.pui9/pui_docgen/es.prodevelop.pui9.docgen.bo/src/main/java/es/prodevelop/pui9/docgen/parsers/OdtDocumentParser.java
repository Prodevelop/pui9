package es.prodevelop.pui9.docgen.parsers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.odftoolkit.odfdom.pkg.OdfElement;
import org.odftoolkit.simple.TextDocument;
import org.odftoolkit.simple.common.navigation.InvalidNavigationException;
import org.odftoolkit.simple.common.navigation.TextNavigation;
import org.odftoolkit.simple.common.navigation.TextSelection;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.w3c.dom.Node;

import com.google.common.io.Files;

import es.prodevelop.pui9.docgen.dto.MappingDto;
import es.prodevelop.pui9.file.FileDownload;
import es.prodevelop.pui9.login.PuiUserSession;
import es.prodevelop.pui9.model.dto.DtoRegistry;
import es.prodevelop.pui9.model.dto.interfaces.IDto;

/**
 * This is a Document Parser for ODT files. It uses the "simple-odf" library
 * from Apache
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
@Component
public class OdtDocumentParser extends AbstractDocumentParser {

	private OdtDocumentParser() {
		DocumentParserRegistry.getSingleton().registerDocumentParser(this);
	}

	@Override
	public String getFileExtension() {
		return "odt";
	}

	@Override
	public List<String> getTags(InputStream inputStream) {
		TextDocument doc = null;
		try {
			doc = TextDocument.loadDocument(inputStream);

			Set<String> tags = new HashSet<>();
			tags.addAll(getTagsFromElement(doc.getContentRoot()));
			tags.addAll(getTagsFromElement(doc.getHeader().getOdfElement()));
			tags.addAll(getTagsFromElement(doc.getFooter().getOdfElement()));

			return new ArrayList<>(tags);
		} catch (Exception e) {
			return Collections.emptyList();
		} finally {
			if (doc != null) {
				doc.close();
			}
		}
	}

	private Set<String> getTagsFromElement(OdfElement element) {
		String content = element.getTextContent();

		if (StringUtils.isEmpty(content)) {
			return Collections.emptySet();
		}

		Set<String> tags = new HashSet<>();
		Pattern pattern = Pattern.compile(TAG_REGEX);
		Matcher match = pattern.matcher(content);
		while (match.find()) {
			String tag = match.group(1);
			if (!tags.contains(tag)) {
				tags.add(tag);
			}
		}

		return tags;
	}

	@Override
	public FileDownload parse(File file, List<IDto> list, List<String> pkFields, List<MappingDto> mapping,
			List<String> columnsFilename, boolean isGeneratePdf) throws Exception {
		DateTimeFormatter oldFormatter = formatter;
		if (PuiUserSession.getCurrentSession() != null) {
			formatter = DateTimeFormatter.ofPattern(PuiUserSession.getCurrentSession().getDateformat() + " HH:mm")
					.withZone(PuiUserSession.getCurrentSession().getTimezone());
		}

		boolean hasDetails = checkDocumentHasDetails(file);
		List<DtoDetailsElement> items = parseDtos(list, pkFields, hasDetails);
		boolean oneDocumentPerRegistry = !CollectionUtils.isEmpty(columnsFilename);

		FileDownload fd;
		if (oneDocumentPerRegistry) {
			fd = parseOneDocumentPerRegistry(file, items, mapping, columnsFilename, hasDetails, isGeneratePdf);
		} else {
			fd = parseOneDocumentForAll(file, items, mapping, hasDetails, isGeneratePdf);
		}

		formatter = oldFormatter;

		return fd;
	}

	private FileDownload parseOneDocumentForAll(File file, List<DtoDetailsElement> items, List<MappingDto> mapping,
			boolean hasDetails, boolean isGeneratePdf) throws Exception {
		// parse the document for each DTO
		TextDocument document = null;
		for (Iterator<DtoDetailsElement> it = items.iterator(); it.hasNext();) {
			DtoDetailsElement next = it.next();

			TextDocument doc = TextDocument.loadDocument(file);
			processDocument(doc, next, mapping, hasDetails);

			if (document == null) {
				document = doc;
			} else {
				document.insertContentFromDocumentAfter(doc, document.getParagraphByReverseIndex(0, false), true);
				doc.close();
			}

			if (it.hasNext() && isGeneratePdf) {
				document.addPageBreak();
			}
		}

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		if (document != null) {
			document.save(out);
			document.close();
		}

		InputStream is = new ByteArrayInputStream(out.toByteArray());
		out.close();

		InputStream isFinal;
		if (isGeneratePdf) {
			isFinal = getPdfConverter().convert(is);
		} else {
			isFinal = is;
		}

		String name = Files.getNameWithoutExtension(file.getName());
		String extension;
		if (isGeneratePdf) {
			extension = "pdf";
		} else {
			extension = getFileExtension();
		}

		name = name.substring(0, name.lastIndexOf('_'));

		return new FileDownload(isFinal, name + "." + extension);
	}

	private FileDownload parseOneDocumentPerRegistry(File file, List<DtoDetailsElement> items, List<MappingDto> mapping,
			List<String> columnsFilename, boolean hasDetails, boolean isGeneratePdf) throws Exception {
		String itemExtension;
		if (isGeneratePdf) {
			itemExtension = "pdf";
		} else {
			itemExtension = getFileExtension();
		}

		Map<String, Integer> mapDocumentTimes = new HashMap<>();

		String zipFilename = Files.getNameWithoutExtension(file.getName());
		zipFilename = zipFilename.substring(0, zipFilename.lastIndexOf('_')) + ".zip";
		File tmpFile = new File(FileUtils.getTempDirectory(), zipFilename);
		try (ZipArchiveOutputStream zaos = new ZipArchiveOutputStream(tmpFile)) {
			for (Iterator<DtoDetailsElement> it = items.iterator(); it.hasNext();) {
				DtoDetailsElement next = it.next();

				TextDocument document = TextDocument.loadDocument(file);
				processDocument(document, next, mapping, hasDetails);

				ByteArrayOutputStream out = new ByteArrayOutputStream();
				document.save(out);
				document.close();

				InputStream is = new ByteArrayInputStream(out.toByteArray());
				out.close();

				InputStream isFinal;
				if (isGeneratePdf) {
					isFinal = getPdfConverter().convert(is);
				} else {
					isFinal = is;
				}

				try {
					StringBuilder nameSb = new StringBuilder();
					for (Iterator<String> itName = columnsFilename.iterator(); itName.hasNext();) {
						String columnFilename = itName.next();
						Field nameField = DtoRegistry.getJavaFieldFromAllFields(next.getDto().getClass(),
								columnFilename);
						Object val = nameField.get(next.getDto());
						String partialName = "";
						if (val != null) {
							partialName = val.toString();
						}
						nameSb.append(partialName);
						if (itName.hasNext()) {
							nameSb.append("_");
						}
					}

					String name = nameSb.toString();

					mapDocumentTimes.put(name, mapDocumentTimes.getOrDefault(name, -1) + 1);
					Integer times = mapDocumentTimes.get(name);
					String suffix = times == 0 ? "" : "_" + times;

					zaos.putArchiveEntry(new ZipArchiveEntry(name + suffix + "." + itemExtension));
					IOUtils.copy(isFinal, zaos);
					zaos.closeArchiveEntry();
				} finally {
					isFinal.close();
				}
			}

			InputStream is = new ByteArrayInputStream(IOUtils.toByteArray(new FileInputStream(tmpFile)));
			FileUtils.deleteQuietly(tmpFile);

			return new FileDownload(is, zipFilename);
		}
	}

	private boolean checkDocumentHasDetails(File file) throws Exception {
		TextDocument document = TextDocument.loadDocument(file);
		boolean hasDetails = documentHasDetails(document);
		document.close();
		return hasDetails;
	}

	/**
	 * Process the given document
	 */
	private void processDocument(TextDocument document, DtoDetailsElement dde, List<MappingDto> mapping,
			boolean hasDetails) throws InvalidNavigationException {
		if (hasDetails) {
			processDetails(document, dde, mapping);
		}
		processMainDocument(document, dde, mapping);
	}

	/**
	 * Process the details of the document
	 */
	private void processDetails(TextDocument document, DtoDetailsElement dde, List<MappingDto> mapping)
			throws InvalidNavigationException {
		OdfElement rowIni = getRowIniText(document).getElement();
		OdfElement rowEnd = getRowEndText(document).getElement();
		Node parentNode = rowIni.getParentNode();

		// collect all the nodes that are involved in the detail section
		List<Node> detailNodes = new ArrayList<>();
		int rowIniIndex = -1;
		for (int i = 0; i < parentNode.getChildNodes().getLength(); i++) {
			Node child = parentNode.getChildNodes().item(i);
			if (child.equals(rowEnd)) {
				break;
			}
			if (child.equals(rowIni)) {
				rowIniIndex = i;
				continue;
			}
			if (rowIniIndex >= 0) {
				detailNodes.add(child);
			}
		}

		// clone and duplicate all the detail nodes, one for each detail (minus
		// one, because the existing nodes are valid ;-) )
		for (int i = 1; i < dde.getDetails().size(); i++) {
			for (Node detailNode : detailNodes) {
				Node clonedNode = detailNode.cloneNode(true);
				parentNode.insertBefore(clonedNode, rowEnd);
			}
		}

		// for each detail and mapping,
		for (int i = 0; i < dde.getDetails().size(); i++) {
			for (MappingDto mapp : mapping) {
				String tag = TAG_PREFIX + mapp.getTag() + TAG_SUFFIX;
				String value = getValue(mapp, dde.getDetails().get(i));
				substituteTagByText(document, tag, value, true);
			}
		}

		// remove the markers of the detail section
		parentNode.removeChild(rowIni);
		parentNode.removeChild(rowEnd);
	}

	/**
	 * Process the main document
	 */
	private void processMainDocument(TextDocument document, DtoDetailsElement dde, List<MappingDto> mapping)
			throws InvalidNavigationException {
		for (MappingDto mapp : mapping) {
			String tag = TAG_PREFIX + mapp.getTag() + TAG_SUFFIX;
			String value = getValue(mapp, dde.getDto());
			substituteTagByText(document, tag, value, false);
		}
	}

	/**
	 * Do the substitution of the given tag with the given value in the document.
	 * You can specify if only the first matching should be substituted or all the
	 * matchings
	 */
	private void substituteTagByText(TextDocument document, String tag, String value, boolean onlyOne)
			throws InvalidNavigationException {
		TextNavigation searchText = new TextNavigation(tag, document);
		while (searchText.hasNext()) {
			TextSelection item = (TextSelection) searchText.nextSelection();
			item.replaceWith(value);
			if (onlyOne) {
				break;
			}
		}
	}

	/**
	 * Check if the given document has details
	 */
	private boolean documentHasDetails(TextDocument document) {
		TextSelection itemIni = getRowIniText(document);
		TextSelection itemEnd = getRowEndText(document);

		return itemIni != null && itemEnd != null;
	}

	/**
	 * Obtain the row init node
	 */
	private TextSelection getRowIniText(TextDocument document) {
		TextNavigation searchRowIni = new TextNavigation(ROW_INI, document);
		return (TextSelection) searchRowIni.nextSelection();
	}

	/**
	 * Obtain the row end node
	 */
	private TextSelection getRowEndText(TextDocument document) {
		TextNavigation searchRowEnd = new TextNavigation(ROW_END, document);
		return (TextSelection) searchRowEnd.nextSelection();
	}

}

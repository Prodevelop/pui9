package es.prodevelop.pui9.documents.service;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.SystemUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import es.prodevelop.pui9.common.enums.PuiVariableValues;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiVariable;
import es.prodevelop.pui9.common.service.interfaces.IPuiVariableService;
import es.prodevelop.pui9.documents.exceptions.PuiDocumentsExtensionsException;
import es.prodevelop.pui9.documents.exceptions.PuiDocumentsFileSizeException;
import es.prodevelop.pui9.documents.exceptions.PuiDocumentsThumbnailException;
import es.prodevelop.pui9.documents.exceptions.PuiDocumentsUpdateException;
import es.prodevelop.pui9.documents.exceptions.PuiDocumentsUploadException;
import es.prodevelop.pui9.documents.model.dao.interfaces.IPuiDocumentDao;
import es.prodevelop.pui9.documents.model.dao.interfaces.IPuiDocumentExtensionDao;
import es.prodevelop.pui9.documents.model.dao.interfaces.IPuiDocumentModelExtensionDao;
import es.prodevelop.pui9.documents.model.dao.interfaces.IPuiDocumentRoleDao;
import es.prodevelop.pui9.documents.model.dto.PuiDocument;
import es.prodevelop.pui9.documents.model.dto.interfaces.IPuiDocument;
import es.prodevelop.pui9.documents.model.dto.interfaces.IPuiDocumentPk;
import es.prodevelop.pui9.documents.model.dto.interfaces.IPuiDocumentRole;
import es.prodevelop.pui9.documents.model.dto.interfaces.IPuiDocumentRolePk;
import es.prodevelop.pui9.documents.model.views.dao.interfaces.IVPuiDocumentDao;
import es.prodevelop.pui9.documents.model.views.dto.interfaces.IVPuiDocument;
import es.prodevelop.pui9.documents.service.interfaces.IPuiDocumentService;
import es.prodevelop.pui9.documents.utils.PuiDocumentLite;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.exceptions.PuiDaoSaveException;
import es.prodevelop.pui9.exceptions.PuiServiceDeleteException;
import es.prodevelop.pui9.exceptions.PuiServiceException;
import es.prodevelop.pui9.exceptions.PuiServiceGetException;
import es.prodevelop.pui9.exceptions.PuiServiceInsertException;
import es.prodevelop.pui9.file.FileDownload;
import es.prodevelop.pui9.filter.FilterBuilder;
import es.prodevelop.pui9.model.dto.DtoRegistry;
import es.prodevelop.pui9.model.dto.interfaces.ITableDto;
import es.prodevelop.pui9.service.AbstractService;
import es.prodevelop.pui9.threads.PuiBackgroundExecutors;
import es.prodevelop.pui9.utils.PuiLanguageUtils;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import net.coobird.thumbnailator.util.ThumbnailatorUtils;

/**
 * @generated
 */
@Service
public class PuiDocumentService
		extends AbstractService<IPuiDocumentPk, IPuiDocument, IVPuiDocument, IPuiDocumentDao, IVPuiDocumentDao>
		implements IPuiDocumentService {

	private static final String DOCUMENT_PK_SEPARATOR = "#";
	private static final String ALL_DOCS = "*";
	private static final Long MBYTE = 1L * 1024 * 1024;

	@Autowired
	private IPuiVariableService variableService;

	@Autowired
	private IPuiDocumentRoleDao documentRoleDao;

	@Autowired
	private IPuiDocumentExtensionDao documentExtensionDao;

	@Autowired
	private IPuiDocumentModelExtensionDao documentModelExtensionDao;

	private List<String> modelsWithDocuments = new ArrayList<>();
	private Map<String, List<String>> modelsExtensions = new HashMap<>();
	private Map<String, Integer> extensionsMaxsize = new HashMap<>();
	private List<String> roles = new ArrayList<>();

	@PostConstruct
	private void postConstruct() {
		PuiBackgroundExecutors.getSingleton().registerNewExecutor("RefreshDocumentsInfo", true, 0, 30, TimeUnit.MINUTES,
				this::reloadDocumentsInfo);
	}

	@Override
	protected void beforeInsert(IPuiDocument dto) throws PuiServiceException {
		if (dto.getRole() == null) {
			dto.setRole(IPuiDocumentRole.DEFAULT_ROLE);
		}
		if (dto.getDatetime() == null) {
			dto.setDatetime(Instant.now());
		}
	}

	@Override
	protected void beforeUpdate(IPuiDocument oldDto, IPuiDocument dto) throws PuiServiceException {
		if (!dto.getFilenameorig().equals(oldDto.getFilenameorig())) {
			throw new PuiDocumentsUpdateException();
		}
		if (!dto.getFilename().equals(oldDto.getFilename())) {
			throw new PuiDocumentsUpdateException();
		}
		if (!dto.getDatetime().equals(oldDto.getDatetime())) {
			throw new PuiDocumentsUpdateException();
		}
		if (!dto.getModel().equals(oldDto.getModel())) {
			throw new PuiDocumentsUpdateException();
		}
		if (!dto.getPk().equals(oldDto.getPk())) {
			throw new PuiDocumentsUpdateException();
		}
	}

	@Override
	protected void beforeDelete(IPuiDocument dto) throws PuiServiceException {
		try {
			dto = getByPk(dto.createPk());
			deleteFromFileSystem(dto);
		} catch (PuiServiceGetException e) {
			throw new PuiServiceDeleteException(e);
		}
	}

	@Override
	protected void afterGet(IPuiDocument dto) throws PuiServiceException {
		String baseDocsUrl = variableService.getVariable(PuiVariableValues.DOCUMENTS_BASE_URL.name());
		if (StringUtils.isEmpty(baseDocsUrl) || baseDocsUrl.equals(IPuiVariable.VARIABLE_WITH_NO_VALUE)) {
			return;
		}

		String url = baseDocsUrl + dto.getModel() + "/" + dto.getPk() + "/" + dto.getFilename();
		dto.setUrl(url);
	}

	@Override
	public List<String> getRoles() {
		return roles;
	}

	@Override
	public List<String> getExtensionsForModel(String model) {
		return modelsExtensions.get(model);
	}

	@Override
	public IPuiDocument upload(PuiDocumentLite document) throws PuiDocumentsUploadException, PuiServiceInsertException,
			PuiDocumentsThumbnailException, PuiDocumentsExtensionsException, PuiDocumentsFileSizeException {
		List<String> modelExtensions = getExtensionsForModel(document.getModel());
		if (CollectionUtils.isEmpty(modelExtensions)) {
			throw new PuiDocumentsExtensionsException();
		}

		boolean isGenericDocument = !modelExtensions.contains(document.getFile().getFileExtension());

		// if the extension if not supported, throws an exception
		if (isGenericDocument && !modelExtensions.contains(ALL_DOCS)) {
			throw new PuiDocumentsExtensionsException();
		}

		// if the size of the file is higher than supported, throws an exception
		Integer maxsize = null;
		if (!isGenericDocument) {
			maxsize = extensionsMaxsize.get(document.getFile().getFileExtension());
		} else {
			maxsize = extensionsMaxsize.get(ALL_DOCS);
		}

		if (maxsize != null && document.getFile().getFileSize() > (maxsize * MBYTE)) {
			throw new PuiDocumentsFileSizeException();
		}

		boolean generateThumbnails = variableService.getVariable(Boolean.class,
				PuiVariableValues.DOCUMENTS_THUMBNAILS_GENERATE.name())
				&& isImage(document.getFile().getFileExtension());

		// insert the document into the database
		IPuiDocument puiDocument = new PuiDocument();
		puiDocument.setModel(document.getModel());
		puiDocument.setPk(getPkFromMap(document.getModel(), document.getPk()));
		puiDocument.setDescription(document.getDescription());
		puiDocument.setLanguage(document.getLanguage());
		puiDocument.setFilename(document.getFile().getUniqueFullFileName());
		puiDocument.setFilenameorig(document.getFile().getFullFileName());
		puiDocument.setRole(document.getRole());
		if (generateThumbnails) {
			puiDocument
					.setThumbnails(variableService.getVariable(PuiVariableValues.DOCUMENTS_THUMBNAILS_VALUES.name()));
		}
		if (StringUtils.isEmpty(puiDocument.getPk())) {
			throw new PuiDocumentsUploadException(new Exception("No PK provided"));
		}
		puiDocument = insert(puiDocument);

		String docsPath = getPkDocumentPath(puiDocument.getModel(), puiDocument.getPk());
		if (docsPath == null) {
			throw new PuiDocumentsUploadException();
		}

		// upload the file into the file system
		File originalFile = new File(docsPath + puiDocument.getFilename());
		try {
			byte[] content = IOUtils.toByteArray(document.getFile().getInputStream());
			FileUtils.writeByteArrayToFile(originalFile, content);

			if (SystemUtils.IS_OS_UNIX) {
				Set<PosixFilePermission> permissions = PosixFilePermissions.fromString("rw-r--r--");
				Files.setPosixFilePermissions(originalFile.toPath(), permissions);
			}
		} catch (IOException e) {
			throw new PuiDocumentsUploadException(e);
		}

		if (generateThumbnails) {
			try {
				createThumbnails(puiDocument);
			} catch (PuiDocumentsThumbnailException e) {
				try {
					Files.deleteIfExists(originalFile.toPath());
				} catch (IOException e1) {
					// do nothing
				}

				try {
					delete(puiDocument.createPk());
				} catch (PuiServiceDeleteException e1) {
					// do nothing
				}

				throw e;
			}
		}

		return puiDocument;
	}

	@Override
	public List<IPuiDocument> getDtoDocuments(ITableDto dto) {
		String model = getDaoRegistry().getModelIdFromDto(dto.getClass());
		String pk = getPkFromDto(dto);
		return getDtoDocuments(model, pk, null);
	}

	@Override
	public List<IPuiDocument> getDtoDocuments(String model, String pk, IPuiDocumentRolePk role) {
		FilterBuilder filterBuilder = FilterBuilder.newAndFilter().addEquals(IPuiDocument.PK_COLUMN, pk)
				.addEquals(IPuiDocument.MODEL_COLUMN, model);
		if (role != null) {
			filterBuilder.addEquals(IPuiDocument.ROLE_COLUMN, role.getRole());
		}

		try {
			return getAllWhere(filterBuilder);
		} catch (PuiServiceGetException e) {
			return Collections.emptyList();
		}
	}

	@Override
	public IPuiDocument getDocumentFromFileName(String model, String pk, String filename) {
		FilterBuilder filterBuilder = FilterBuilder.newAndFilter().addEquals(IPuiDocument.MODEL_COLUMN, model)
				.addEquals(IPuiDocument.PK_COLUMN, pk).addEquals(IPuiDocument.FILENAME_COLUMN, filename);
		List<IPuiDocument> list;
		try {
			list = getAllWhere(filterBuilder);
		} catch (PuiServiceGetException e) {
			list = Collections.emptyList();
		}

		return !list.isEmpty() ? list.get(0) : null;
	}

	@Override
	public boolean existsDocumentFile(IPuiDocument dto) {
		FileDownload fd = getFile(dto.createPk());
		return fd != null && fd.getFile().exists();
	}

	@Override
	public FileDownload getFile(IPuiDocumentPk pk) {
		IPuiDocument dto;
		try {
			dto = getByPk(pk);
		} catch (PuiServiceGetException e) {
			return null;
		}

		String docsPath = getPkDocumentPath(dto.getModel(), dto.getPk());
		if (docsPath == null) {
			return null;
		}

		String filePath = docsPath + dto.getFilename();

		return new FileDownload(new File(filePath), dto.getFilenameorig());
	}

	@Override
	public List<String> getModelsWithDocuments() {
		return modelsWithDocuments;
	}

	@Override
	public void reloadDocumentsInfo() {
		modelsWithDocuments.clear();
		modelsExtensions.clear();
		extensionsMaxsize.clear();
		roles.clear();

		try {
			documentModelExtensionDao.findAll().forEach(dme -> {
				if (!modelsWithDocuments.contains(dme.getModel())) {
					modelsWithDocuments.add(dme.getModel());
				}
				if (!modelsExtensions.containsKey(dme.getModel())) {
					modelsExtensions.put(dme.getModel(), new ArrayList<>());
				}
				modelsExtensions.get(dme.getModel()).add(dme.getExtension().toLowerCase());
			});
			documentExtensionDao.findAll()
					.forEach(de -> extensionsMaxsize.put(de.getExtension().toLowerCase(), de.getMaxsize()));
			documentRoleDao.findAll(PuiLanguageUtils.getDefaultLanguage()).forEach(dr -> roles.add(dr.getRole()));
		} catch (PuiDaoFindException e) {
			// do nothing
		}

		Collections.sort(modelsWithDocuments);
		Collections.sort(roles);
	}

	@Override
	public void reloadThumbnails() {
		new Thread(() -> {
			List<IPuiDocument> all;
			try {
				all = getTableDao().findAll();
			} catch (PuiDaoFindException e) {
				all = Collections.emptyList();
			}

			String thumbnailValues = variableService.getVariable(PuiVariableValues.DOCUMENTS_THUMBNAILS_VALUES.name());

			for (IPuiDocument document : all) {
				if (!isImage(document)) {
					continue;
				}

				deleteThumbnails(document);
				try {
					document.setThumbnails(thumbnailValues);
					getTableDao().save(document);
					createThumbnails(document);
				} catch (PuiDocumentsThumbnailException | PuiDaoSaveException e) {
					// nothing to do
				}
			}
		}, "PuiThread_PuiDocumentReloadThumbnails").start();
	}

	private String getPkDocumentPath(String model, String pk) {
		String basePath = getBaseDocumentsPath();
		basePath += model + File.separator + pk + File.separator;

		File folders = new File(basePath);
		boolean createFolders = folders.mkdirs();
		if (!createFolders && !folders.exists()) {
			return null;
		}

		return basePath;
	}

	@Override
	public String getBaseDocumentsPath() {
		String docsPath = variableService.getVariable(PuiVariableValues.DOCUMENTS_PATH.name());
		if (!docsPath.endsWith(File.separator)) {
			docsPath += File.separator;
		}

		String tagStart = "[$][{]";
		String tagEnd = "[}]";
		String tagRegex = tagStart + "([^{]*)" + tagEnd;

		List<String> allMatches = new ArrayList<>();
		Matcher m = Pattern.compile(tagRegex).matcher(docsPath);
		while (m.find()) {
			allMatches.add(m.group());
		}

		String basePath = docsPath;
		for (String match : allMatches) {
			String prop = match.replaceAll(tagStart, "").replaceAll(tagEnd, "");
			String propVal = System.getProperty(prop);
			if (propVal != null) {
				basePath = basePath.replace(match, propVal);
			}
		}

		return basePath;
	}

	private String getPkFromMap(String model, Map<String, Object> pkMap) {
		Class<ITableDto> dtoClass = getDaoRegistry().getTableDtoFromModelId(model, true);
		StringBuilder pk = new StringBuilder();
		List<String> pkColumnNames = DtoRegistry.getAllColumnNames(dtoClass);
		List<String> pkFieldNames = DtoRegistry.getAllFields(dtoClass);

		for (Iterator<String> it = pkMap.keySet().iterator(); it.hasNext();) {
			String next = it.next();
			if (pkColumnNames.contains(next.toLowerCase()) || pkFieldNames.contains(next.toLowerCase())) {
				pk.append(pkMap.get(next));
				if (it.hasNext()) {
					pk.append(DOCUMENT_PK_SEPARATOR);
				}
			}
		}

		return pk.toString();
	}

	private String getPkFromDto(ITableDto dto) {
		ITableDto dtoPk = dto.createPk();
		StringBuilder pk = new StringBuilder();

		for (Iterator<Field> it = DtoRegistry.getAllJavaFields(dtoPk.getClass()).iterator(); it.hasNext();) {
			Field next = it.next();
			try {
				Object obj = FieldUtils.readField(next, dtoPk, true);
				pk.append(obj);
				if (it.hasNext()) {
					pk.append(DOCUMENT_PK_SEPARATOR);
				}
			} catch (Exception e) {
				// do nothing
			}
		}

		return pk.toString();
	}

	private void deleteFromFileSystem(IPuiDocument document) {
		String docsPath = getPkDocumentPath(document.getModel(), document.getPk());
		if (docsPath == null) {
			return;
		}

		// delete main file
		String filePath = docsPath + document.getFilename();
		File file = new File(filePath);
		try {
			Files.deleteIfExists(file.toPath());
		} catch (IOException e1) {
			logger.error("Error while deleting the document file");
		}

		// if it's an image, delete all the thumbnails
		if (isImage(document)) {
			deleteThumbnails(document);
		}

		File docsFolder = new File(docsPath);
		File[] files = docsFolder.listFiles();
		if (files != null && files.length == 0) {
			try {
				Files.deleteIfExists(docsFolder.toPath());
			} catch (IOException e1) {
				logger.error("Error while deleting the documents folder");
			}
		}
	}

	private void createThumbnails(IPuiDocument document) throws PuiDocumentsThumbnailException {
		String docsPath = getPkDocumentPath(document.getModel(), document.getPk());
		boolean cropThumbnails = variableService.getVariable(Boolean.class,
				PuiVariableValues.DOCUMENTS_THUMBNAILS_CROP.name());
		List<File> files = new ArrayList<>();
		File originalFile = new File(docsPath + document.getFilename());

		try {
			for (String size : getThumbnailsSizes(document.getThumbnails())) {
				String thumbnailFolderPath = docsPath + size + File.separator;
				File thumbnailFolder = new File(thumbnailFolderPath);
				thumbnailFolder.mkdirs();
				String thumbnailPath = thumbnailFolderPath + document.getFilename();
				File thumbnailFile = new File(thumbnailPath);
				files.add(thumbnailFile);

				int[] pixels = parseThumbnailSize(size);
				if (cropThumbnails) {
					Thumbnails.of(originalFile).crop(Positions.TOP_CENTER).size(pixels[0], pixels[1])
							.toFile(thumbnailFile);
				} else {
					Thumbnails.of(originalFile).size(pixels[0], pixels[1]).keepAspectRatio(true).toFile(thumbnailFile);
				}
			}
		} catch (Exception e) {
			// error while generating thumbnails: remove everything created
			try {
				for (File file : files) {
					Files.deleteIfExists(file.toPath());
				}
			} catch (Exception e1) {
				logger.error("Error while deleting the document file");
			}

			throw new PuiDocumentsThumbnailException();
		}
	}

	private void deleteThumbnails(IPuiDocument document) {
		String docsPath = getPkDocumentPath(document.getModel(), document.getPk());

		for (String size : getThumbnailsSizes(document.getThumbnails())) {
			String thumbnailFolder = docsPath + size;
			String thumbnailPath = thumbnailFolder + File.separator + document.getFilename();
			File thumbnailFile = new File(thumbnailPath);
			if (thumbnailFile.exists()) {
				try {
					Files.deleteIfExists(thumbnailFile.toPath());
				} catch (IOException e1) {
					logger.error("Error while deleting the thumbnail file");
				}
			}

			File thumbnailFolderFile = new File(thumbnailFolder);
			File[] files = thumbnailFolderFile.listFiles();
			if (files != null && files.length == 0) {
				try {
					Files.deleteIfExists(thumbnailFolderFile.toPath());
				} catch (IOException e1) {
					logger.error("Error while deleting the thumbnail folder");
				}
			}
		}
	}

	private List<String> getThumbnailsSizes(String thumbnails) {
		if (StringUtils.isEmpty(thumbnails)) {
			return Collections.emptyList();
		}

		String[] splits = thumbnails.split(",");
		List<String> list = new ArrayList<>();
		for (String split : splits) {
			list.add(split.trim());
		}

		return list;
	}

	/**
	 * Returns an array of two components: first is the width pixels and second is
	 * the height pixels. [0]x[1]
	 */
	private int[] parseThumbnailSize(String thumbnailSize) {
		Assert.hasText(thumbnailSize, "Thumbnail variable has no value");

		String[] splits = thumbnailSize.trim().split("x");
		int[] pixels = new int[2];
		pixels[0] = Integer.parseInt(splits[0].trim());
		pixels[1] = Integer.parseInt(splits[1].trim());

		return pixels;
	}

	private boolean isImage(IPuiDocument document) {
		return isImage(FilenameUtils.getExtension(document.getFilename()).toLowerCase());
	}

	private boolean isImage(String extension) {
		return ThumbnailatorUtils.isSupportedOutputFormat(extension);
	}

}
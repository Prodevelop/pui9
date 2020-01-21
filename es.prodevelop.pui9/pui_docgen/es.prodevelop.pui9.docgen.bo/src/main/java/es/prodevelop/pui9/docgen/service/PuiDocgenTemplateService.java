package es.prodevelop.pui9.docgen.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.SystemUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.io.Files;
import com.google.common.reflect.TypeToken;

import es.prodevelop.pui9.common.enums.PuiVariableValues;
import es.prodevelop.pui9.common.exceptions.PuiCommonNoFileException;
import es.prodevelop.pui9.common.model.dao.interfaces.IPuiModelDao;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiModel;
import es.prodevelop.pui9.common.service.interfaces.IPuiVariableService;
import es.prodevelop.pui9.docgen.dto.GenerateTemplateRequest;
import es.prodevelop.pui9.docgen.dto.MappingDto;
import es.prodevelop.pui9.docgen.dto.MappingValueDto;
import es.prodevelop.pui9.docgen.dto.ParametersDto;
import es.prodevelop.pui9.docgen.enums.MappingOriginEnum;
import es.prodevelop.pui9.docgen.exceptions.PuiDocgenGenerateException;
import es.prodevelop.pui9.docgen.exceptions.PuiDocgenModelNotExistsException;
import es.prodevelop.pui9.docgen.exceptions.PuiDocgenNoElementsException;
import es.prodevelop.pui9.docgen.exceptions.PuiDocgenNoParserException;
import es.prodevelop.pui9.docgen.exceptions.PuiDocgenUploadingTemplateException;
import es.prodevelop.pui9.docgen.fields.ISystemField;
import es.prodevelop.pui9.docgen.fields.SystemFieldsRegistry;
import es.prodevelop.pui9.docgen.model.dao.interfaces.IPuiDocgenAttributeDao;
import es.prodevelop.pui9.docgen.model.dao.interfaces.IPuiDocgenTemplateDao;
import es.prodevelop.pui9.docgen.model.dto.interfaces.IPuiDocgenAttribute;
import es.prodevelop.pui9.docgen.model.dto.interfaces.IPuiDocgenTemplate;
import es.prodevelop.pui9.docgen.model.dto.interfaces.IPuiDocgenTemplatePk;
import es.prodevelop.pui9.docgen.model.views.dao.interfaces.IVPuiDocgenTemplateDao;
import es.prodevelop.pui9.docgen.model.views.dto.interfaces.IVPuiDocgenTemplate;
import es.prodevelop.pui9.docgen.parsers.DocumentParserRegistry;
import es.prodevelop.pui9.docgen.parsers.IDocumentParser;
import es.prodevelop.pui9.docgen.service.interfaces.IPuiDocgenTemplateService;
import es.prodevelop.pui9.docgen.utils.PuiDocgenTemplateExtended;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.exceptions.PuiException;
import es.prodevelop.pui9.exceptions.PuiServiceException;
import es.prodevelop.pui9.exceptions.PuiServiceGetException;
import es.prodevelop.pui9.exceptions.PuiServiceInsertException;
import es.prodevelop.pui9.file.FileDownload;
import es.prodevelop.pui9.filter.FilterBuilder;
import es.prodevelop.pui9.filter.FilterGroup;
import es.prodevelop.pui9.filter.FilterRule;
import es.prodevelop.pui9.json.GsonSingleton;
import es.prodevelop.pui9.model.dao.interfaces.IDao;
import es.prodevelop.pui9.model.dto.DtoRegistry;
import es.prodevelop.pui9.model.dto.interfaces.IDto;
import es.prodevelop.pui9.model.dto.interfaces.IViewDto;
import es.prodevelop.pui9.service.AbstractService;

/**
 * @generated
 */
@Service
public class PuiDocgenTemplateService extends
		AbstractService<IPuiDocgenTemplatePk, IPuiDocgenTemplate, IVPuiDocgenTemplate, IPuiDocgenTemplateDao, IVPuiDocgenTemplateDao>
		implements IPuiDocgenTemplateService {

	@Autowired
	private IPuiVariableService variableService;

	@Autowired
	private IPuiModelDao puimodelDao;

	@Autowired
	private IPuiDocgenAttributeDao docgenAttributeDao;

	private Type listStringType = new TypeToken<List<String>>() {
		private static final long serialVersionUID = 1L;
	}.getType();
	private Type mappingDtoType = new TypeToken<List<MappingDto>>() {
		private static final long serialVersionUID = 1L;
	}.getType();

	@Override
	protected void afterNew(IPuiDocgenTemplate dto) throws PuiServiceException {
		dto.setFilter(new FilterGroup().toJson());
		dto.setMapping(GsonSingleton.getSingleton().getGson().toJson(new MappingDto[0]));
		dto.setParameters(GsonSingleton.getSingleton().getGson().toJson(new ParametersDto[0]));
		dto.setModels(GsonSingleton.getSingleton().getGson().toJson(Collections.emptyList()));
		dto.setColumnfilename(GsonSingleton.getSingleton().getGson().toJson(Collections.emptyList()));
	}

	@Override
	protected void beforeDelete(IPuiDocgenTemplate dto) throws PuiServiceException {
		// when deleting the template in the database, also delete the file form
		// the file system
		deleteFile(dto);
	}

	@Override
	public List<MappingDto> getTemplateMapping(MultipartFile multipartFile) throws PuiServiceException {
		try {
			List<String> tags = getTags(multipartFile.getOriginalFilename(), multipartFile.getInputStream());
			if (tags == null) {
				return Collections.emptyList();
			}

			// make a MappingDto for each found tag
			List<MappingDto> mappings = new ArrayList<>();
			for (String tag : tags) {
				MappingDto mapping = new MappingDto();
				mapping.setTag(tag);
				mappings.add(mapping);
			}

			return mappings;
		} catch (IOException e) {
			throw new PuiServiceException(e);
		}
	}

	@Override
	public List<String> getSystemFields() {
		return SystemFieldsRegistry.getSingleton().getAllSystemFieldNames();
	}

	@Override
	public void uploadTemplate(PuiDocgenTemplateExtended template)
			throws PuiServiceInsertException, PuiDocgenUploadingTemplateException {
		// upload the file to the file system
		try {
			uploadFile(template);
		} catch (PuiException e) {
			throw new PuiDocgenUploadingTemplateException();
		}

		// and finally, insert the element in the database
		try {
			insert(template.asPuiDocgenTemplate());
		} catch (PuiServiceInsertException e) {
			deleteFile(template);
			throw e;
		}
	}

	@Override
	public FileDownload downloadTemplate(IPuiDocgenTemplatePk dtoPk)
			throws PuiServiceGetException, PuiCommonNoFileException {
		IPuiDocgenTemplate dto = getByPk(dtoPk);
		File file = getTemplateFile(dto);

		String name = Files.getNameWithoutExtension(dto.getFilename());
		String extension = Files.getFileExtension(dto.getFilename());

		// remove timestamp from the filename
		String filename = name.substring(0, name.lastIndexOf('_'));
		filename += "." + extension;

		return new FileDownload(file, filename);
	}

	@Override
	public FileDownload downloadSampleTemplate() throws PuiCommonNoFileException {
		InputStream is = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream(IPuiDocgenTemplateService.SAMPLE_TEMPLATE);
		return new FileDownload(is, IPuiDocgenTemplateService.SAMPLE_TEMPLATE);
	}

	@Override
	public List<String> getModelsWithDocgen() {
		List<IPuiDocgenTemplate> all;
		try {
			all = getTableDao().findAll();
		} catch (PuiDaoFindException e) {
			all = Collections.emptyList();
		}

		Set<String> models = new HashSet<>();
		for (IPuiDocgenTemplate template : all) {
			if (template.getModels() == null) {
				continue;
			}

			List<String> templateModels = GsonSingleton.getSingleton().getGson().fromJson(template.getModels(),
					listStringType);
			templateModels.forEach(model -> models.add(model.trim()));
		}

		List<String> modelsList = new ArrayList<>(models);
		Collections.sort(modelsList);
		return modelsList;
	}

	@Override
	public List<IPuiDocgenTemplate> getMatchingTemplates(String model) throws PuiServiceGetException {
		try {
			// get all the templates that has the same model than the given one
			FilterBuilder filterBuilder = FilterBuilder.newAndFilter().addContains(IPuiDocgenTemplate.MODELS_COLUMN,
					model);
			List<IPuiDocgenTemplate> docgenTemplateList = getTableDao().findWhere(filterBuilder);
			Collections.sort(docgenTemplateList, new PuIPuiDocgenTemplateComparator());

			return docgenTemplateList;
		} catch (PuiDaoFindException e) {
			throw new PuiServiceGetException(e);
		}
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public FileDownload generate(GenerateTemplateRequest req)
			throws PuiServiceGetException, PuiDocgenModelNotExistsException, PuiDocgenNoElementsException,
			PuiDocgenGenerateException, PuiDocgenNoParserException, PuiCommonNoFileException {
		try {
			IPuiDocgenTemplate template = getByPk(req.getPk());
			if (StringUtils.isEmpty(req.getModel())) {
				req.setModel(template.getMainmodel());
			}
			IDao dao = getDaoFromModel(req.getModel());
			FilterBuilder finalFilterBuilder = getFinalFilter(template, req.buildSearchFilter(dao.getDtoClass()),
					req.getParameters());

			List<IDto> list = dao.findWhere(finalFilterBuilder);
			if (list.isEmpty()) {
				throw new PuiDocgenNoElementsException();
			}

			return doGenerate(req.getModel(), template, req.getMappings(), list, req.isGeneratePdf());
		} catch (PuiDaoFindException e) {
			throw new PuiServiceGetException(e);
		}
	}

	private File getTemplateFile(IPuiDocgenTemplate template) throws PuiCommonNoFileException {
		String path = getDocgenPath();
		path += template.getFilename();
		File file = new File(path);
		if (!file.exists()) {
			throw new PuiCommonNoFileException();
		}

		return file;
	}

	private IDocumentParser getDocumentParser(String filename) throws PuiDocgenNoParserException {
		IDocumentParser parser = DocumentParserRegistry.getSingleton().getDocumentParser(filename);
		if (parser == null) {
			throw new PuiDocgenNoParserException(filename);
		}
		return parser;
	}

	private List<MappingDto> modifyMappingsWithUserValues(IPuiDocgenTemplate template,
			List<MappingValueDto> mappingValueList) throws PuiServiceGetException {
		try {
			// modify the mappings with the user values
			List<IPuiDocgenAttribute> tableAttributes = docgenAttributeDao.findAll();
			List<MappingDto> mapping = GsonSingleton.getSingleton().getGson().fromJson(template.getMapping(),
					mappingDtoType);
			for (MappingDto mapp : mapping) {
				if (mapp.getOrigin().equals(MappingOriginEnum.USER.getCode())) {
					for (Iterator<MappingValueDto> it = mappingValueList.iterator(); it.hasNext();) {
						MappingValueDto next = it.next();
						if (mapp.getField().equals(next.getField())) {
							mapp.setField(next.getVal());
						}
					}
				} else if (mapp.getOrigin().equals(MappingOriginEnum.TABLE.getCode())) {
					for (IPuiDocgenAttribute tableAttr : tableAttributes) {
						if (mapp.getField().equals(tableAttr.getId())) {
							mapp.setField(tableAttr.getValue());
						}
					}
				} else if (mapp.getOrigin().equals(MappingOriginEnum.SYSTEM.getCode())) {
					ISystemField systemField = SystemFieldsRegistry.getSingleton().getSystemField(mapp.getField());
					mapp.setField(systemField != null ? systemField.getValue() : "");
				}
			}
			return mapping;
		} catch (PuiDaoFindException e) {
			throw new PuiServiceGetException(e);
		}
	}

	/**
	 * Upload the given template file (specified with bytes) to the file system. It
	 * appends the current timestamp to the filename, in order to avoid duplicates
	 */
	private void uploadFile(PuiDocgenTemplateExtended template) throws PuiException {
		String docgenPath = getDocgenPath();

		File folders = new File(docgenPath);
		boolean createFolders = folders.mkdirs();
		if (!createFolders && !folders.exists()) {
			throw new PuiException("Could not create 'docgen' folder");
		}

		String name = FilenameUtils.getBaseName(template.getFilename());
		String extension = FilenameUtils.getExtension(template.getFilename());

		// add timestamp to the filename
		String filename = name + "_" + System.currentTimeMillis() + "." + extension;
		template.setFilename(filename);

		String filePath = docgenPath + template.getFilename();
		File file = new File(filePath);
		try {
			java.nio.file.Files.deleteIfExists(file.toPath());
		} catch (IOException e1) {
			logger.error("Error while deleting the docgen template");
		}

		try {
			Files.write(template.getFile().getBytes(), file);

			if (SystemUtils.IS_OS_UNIX) {
				Set<PosixFilePermission> permissions = PosixFilePermissions.fromString("rw-r--r--");
				java.nio.file.Files.setPosixFilePermissions(file.toPath(), permissions);
			}
		} catch (IOException e) {
			throw new PuiException(e);
		}
	}

	/**
	 * Deletes the associated file of the Template from the file system
	 */
	private void deleteFile(IPuiDocgenTemplate dto) {
		String docgenPath = getDocgenPath();
		String filePath = docgenPath + dto.getFilename();
		File file = new File(filePath);
		try {
			java.nio.file.Files.deleteIfExists(file.toPath());
		} catch (IOException e1) {
			logger.error("Error while deleting the docgen template");
		}
	}

	/**
	 * Get the list of tags of the given File (represented by the InputStream)
	 */
	private List<String> getTags(String fileName, InputStream inputStream) throws PuiDocgenNoParserException {
		return getDocumentParser(fileName).getTags(inputStream);
	}

	private String getDocgenPath() {
		String docsPath = variableService.getVariable(PuiVariableValues.DOCGEN_PATH.name());
		if (!docsPath.endsWith("/")) {
			docsPath += "/";
		}

		String tagStart = "[$][{]";
		String tagEnd = "[}]";
		String tagRegex = tagStart + "([^{]*)" + tagEnd;

		List<String> allMatches = new ArrayList<>();
		Matcher m = Pattern.compile(tagRegex).matcher(docsPath);
		while (m.find()) {
			allMatches.add(m.group());
		}

		String replaced = docsPath;
		for (String match : allMatches) {
			String prop = match.replaceAll(tagStart, "").replaceAll(tagEnd, "");
			String propVal = System.getProperty(prop);
			if (propVal != null) {
				replaced = replaced.replace(match, propVal);
			}
		}

		return replaced;
	}

	private FilterBuilder getTemplateFilterBuilder(IPuiDocgenTemplate template) {
		FilterGroup templateFilter = new FilterGroup();

		FilterGroup filter = FilterGroup.fromJson(template.getFilter());
		if (filter != null && !StringUtils.isEmpty(filter.toString())) {
			templateFilter.addGroup(filter);
		}

		return FilterBuilder.newFilter(templateFilter);
	}

	private FilterBuilder getFinalFilter(IPuiDocgenTemplate template, FilterBuilder gridFilterBuilder,
			List<FilterRule> parameters) {
		FilterBuilder templateFilterBuilder = getTemplateFilterBuilder(template);

		if (gridFilterBuilder != null && !StringUtils.isEmpty(gridFilterBuilder.toString())) {
			templateFilterBuilder.addGroup(gridFilterBuilder);
		}

		if (!CollectionUtils.isEmpty(parameters)) {
			FilterGroup parametersFilter = new FilterGroup();
			parameters.forEach(parametersFilter::addRule);

			templateFilterBuilder.addGroup(FilterBuilder.newFilter(parametersFilter));
		}

		return templateFilterBuilder;
	}

	private FileDownload doGenerate(String modelName, IPuiDocgenTemplate template,
			List<MappingValueDto> mappingValueList, List<IDto> list, boolean isGeneratePdf)
			throws PuiDocgenNoParserException, PuiCommonNoFileException, PuiServiceGetException,
			PuiDocgenModelNotExistsException, PuiDocgenGenerateException {
		IDocumentParser parser = getDocumentParser(template.getFilename());
		File file = getTemplateFile(template);
		List<String> pkFields = getPkFieldNamesList(modelName);
		List<MappingDto> mapping = modifyMappingsWithUserValues(template, mappingValueList);

		List<String> columnFilenames = GsonSingleton.getSingleton().getGson().fromJson(template.getColumnfilename(),
				listStringType);

		try {
			return parser.parse(file, list, pkFields, mapping, columnFilenames, isGeneratePdf);
		} catch (Exception e) {
			throw new PuiDocgenGenerateException(e);
		}
	}

	@SuppressWarnings("unchecked")
	private List<String> getPkFieldNamesList(String model)
			throws PuiServiceGetException, PuiDocgenModelNotExistsException {
		List<IPuiModel> puiModels;
		try {
			puiModels = puimodelDao.findByModel(model);
		} catch (PuiDaoFindException e) {
			throw new PuiServiceGetException(e);
		}

		if (CollectionUtils.isEmpty(puiModels)) {
			throw new PuiDocgenModelNotExistsException(model);
		}

		String entityName = puiModels.get(0).getEntity();

		Class<? extends IDto> dtoClass = getDaoRegistry().getDtoFromEntityName(entityName, false, true);
		if (dtoClass == null) {
			throw new PuiDocgenModelNotExistsException(model);
		}

		if (IViewDto.class.isInstance(dtoClass)) {
			dtoClass = getServiceRegistry().getTableDtoFromViewDto((Class<IViewDto>) dtoClass);
		}

		if (dtoClass == null) {
			throw new PuiDocgenModelNotExistsException(model);
		}

		return DtoRegistry.getPkFields(dtoClass);
	}

	@SuppressWarnings("rawtypes")
	private IDao getDaoFromModel(String model) throws PuiDocgenModelNotExistsException {
		Class<? extends IDao> daoClass = getDaoClassFromModel(model);
		IDao dao = getContext().getBean(daoClass);
		if (dao == null) {
			throw new PuiDocgenModelNotExistsException(model);
		}

		return dao;
	}

	@SuppressWarnings("rawtypes")
	private Class<? extends IDao> getDaoClassFromModel(String model) throws PuiDocgenModelNotExistsException {
		List<IPuiModel> puiModels;
		try {
			puiModels = puimodelDao.findByModel(model);
		} catch (PuiDaoFindException e) {
			throw new PuiDocgenModelNotExistsException(model);
		}

		if (CollectionUtils.isEmpty(puiModels)) {
			throw new PuiDocgenModelNotExistsException(model);
		}

		Class<? extends IDao> daoClass = getDaoRegistry().getDaoFromEntityName(puiModels.get(0).getEntity(), false);
		if (daoClass == null) {
			throw new PuiDocgenModelNotExistsException(model);
		}

		return daoClass;
	}

	/**
	 * Compare the templates by its name
	 */
	private class PuIPuiDocgenTemplateComparator implements Comparator<IPuiDocgenTemplate> {

		@Override
		public int compare(IPuiDocgenTemplate o1, IPuiDocgenTemplate o2) {
			return o1.getName().compareTo(o2.getName());
		}

	}

}
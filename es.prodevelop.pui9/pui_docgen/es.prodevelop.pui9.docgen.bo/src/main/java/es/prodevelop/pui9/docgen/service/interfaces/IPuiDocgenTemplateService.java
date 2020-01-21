package es.prodevelop.pui9.docgen.service.interfaces;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import es.prodevelop.pui9.common.exceptions.PuiCommonNoFileException;
import es.prodevelop.pui9.docgen.dto.GenerateTemplateRequest;
import es.prodevelop.pui9.docgen.dto.MappingDto;
import es.prodevelop.pui9.docgen.exceptions.PuiDocgenGenerateException;
import es.prodevelop.pui9.docgen.exceptions.PuiDocgenModelNotExistsException;
import es.prodevelop.pui9.docgen.exceptions.PuiDocgenNoElementsException;
import es.prodevelop.pui9.docgen.exceptions.PuiDocgenNoParserException;
import es.prodevelop.pui9.docgen.exceptions.PuiDocgenUploadingTemplateException;
import es.prodevelop.pui9.docgen.model.dao.interfaces.IPuiDocgenTemplateDao;
import es.prodevelop.pui9.docgen.model.dto.interfaces.IPuiDocgenTemplate;
import es.prodevelop.pui9.docgen.model.dto.interfaces.IPuiDocgenTemplatePk;
import es.prodevelop.pui9.docgen.model.views.dao.interfaces.IVPuiDocgenTemplateDao;
import es.prodevelop.pui9.docgen.model.views.dto.interfaces.IVPuiDocgenTemplate;
import es.prodevelop.pui9.docgen.utils.PuiDocgenTemplateExtended;
import es.prodevelop.pui9.exceptions.PuiServiceException;
import es.prodevelop.pui9.exceptions.PuiServiceGetException;
import es.prodevelop.pui9.exceptions.PuiServiceInsertException;
import es.prodevelop.pui9.file.FileDownload;
import es.prodevelop.pui9.service.interfaces.IService;

/**
 * @generated
 */
public interface IPuiDocgenTemplateService extends
		IService<IPuiDocgenTemplatePk, IPuiDocgenTemplate, IVPuiDocgenTemplate, IPuiDocgenTemplateDao, IVPuiDocgenTemplateDao> {

	String SAMPLE_TEMPLATE = "docgen_template.odt";

	/**
	 * Get the tags mapping that are found in the given template
	 */
	List<MappingDto> getTemplateMapping(MultipartFile multipartFile) throws PuiServiceException;

	/**
	 * Get all the available System Fields to be used in the templates
	 */
	List<String> getSystemFields();

	/**
	 * Save the given Template associated with the given template file
	 */
	void uploadTemplate(PuiDocgenTemplateExtended template)
			throws PuiServiceInsertException, PuiDocgenUploadingTemplateException;

	/**
	 * Return the template
	 */
	FileDownload downloadTemplate(IPuiDocgenTemplatePk dtoPk) throws PuiServiceGetException, PuiCommonNoFileException;

	/**
	 * Return the sample template
	 */
	FileDownload downloadSampleTemplate() throws PuiCommonNoFileException;

	/**
	 * Get the list of models that should have the docgen action enabled, because
	 * have any template associated
	 * 
	 * @return The list of model names
	 */
	List<String> getModelsWithDocgen();

	/**
	 * Get all the matching templates that can be applied to the given model.
	 * Additionally, the PK of a selected element can be given, filtering the
	 * resulting templates that can be applied to this element
	 */
	List<IPuiDocgenTemplate> getMatchingTemplates(String model) throws PuiServiceGetException;

	/**
	 * Generates the given Template with the given parameters and mappings.
	 * Additionally, the PK of a selected element can be given, generating the
	 * template only for this element
	 */
	FileDownload generate(GenerateTemplateRequest req)
			throws PuiServiceGetException, PuiDocgenModelNotExistsException, PuiDocgenNoElementsException,
			PuiDocgenGenerateException, PuiDocgenNoParserException, PuiCommonNoFileException;

}
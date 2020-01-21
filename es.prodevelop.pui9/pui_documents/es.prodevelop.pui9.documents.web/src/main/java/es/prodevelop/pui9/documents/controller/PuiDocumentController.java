package es.prodevelop.pui9.documents.controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import es.prodevelop.pui9.annotations.PuiFunctionality;
import es.prodevelop.pui9.controller.AbstractCommonController;
import es.prodevelop.pui9.documents.exceptions.PuiDocumentsExtensionsException;
import es.prodevelop.pui9.documents.exceptions.PuiDocumentsFileSizeException;
import es.prodevelop.pui9.documents.exceptions.PuiDocumentsThumbnailException;
import es.prodevelop.pui9.documents.exceptions.PuiDocumentsUploadException;
import es.prodevelop.pui9.documents.model.dao.interfaces.IPuiDocumentDao;
import es.prodevelop.pui9.documents.model.dto.PuiDocumentRolePk;
import es.prodevelop.pui9.documents.model.dto.interfaces.IPuiDocument;
import es.prodevelop.pui9.documents.model.dto.interfaces.IPuiDocumentPk;
import es.prodevelop.pui9.documents.model.views.dao.interfaces.IVPuiDocumentDao;
import es.prodevelop.pui9.documents.model.views.dto.interfaces.IVPuiDocument;
import es.prodevelop.pui9.documents.service.interfaces.IPuiDocumentService;
import es.prodevelop.pui9.documents.utils.PuiDocumentLite;
import es.prodevelop.pui9.exceptions.PuiServiceInsertException;
import es.prodevelop.pui9.file.FileDownload;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * @generated
 */
@Controller
@Api(tags = "PUI Document")
@RequestMapping("/puidocument")
public class PuiDocumentController extends
		AbstractCommonController<IPuiDocumentPk, IPuiDocument, IVPuiDocument, IPuiDocumentDao, IVPuiDocumentDao, IPuiDocumentService> {
	/**
	 * @generated
	 */
	@Override
	protected String getReadFunctionality() {
		return "READ_PUI_DOCUMENT";
	}

	/**
	 * @generated
	 */
	@Override
	protected String getWriteFunctionality() {
		return "WRITE_PUI_DOCUMENT";
	}

	@Override
	public boolean allowInsert() {
		return false;
	}

	@Override
	public boolean allowTemplate() {
		return true;
	}

	@Override
	public boolean allowExport() {
		return false;
	}

	@ApiOperation(value = "Get all the available document roles", notes = "Get all the available document roles")
	@GetMapping(value = "/getRoles", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<String> getRoles() {
		return getService().getRoles();
	}

	@ApiOperation(value = "Get all the available extension for the given model", notes = "Get all the available extension for the given model")
	@GetMapping(value = "/getExtensionsForModel", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<String> getExtensionsForModel(@RequestParam String model) {
		return getService().getExtensionsForModel(model);
	}

	@ApiOperation(value = "Get all the documents of a registry", notes = "Get all the documents of a registry")
	@GetMapping(value = "/getDocuments", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<IPuiDocument> getDocuments(@RequestParam String model, @RequestParam String pk,
			@RequestParam(required = false) String role) {
		return getService().getDtoDocuments(model, pk, StringUtils.isEmpty(role) ? null : new PuiDocumentRolePk(role));
	}

	@PuiFunctionality(id = ID_FUNCTIONALITY_INSERT, value = METHOD_FUNCTIONALITY_INSERT)
	@ApiOperation(value = "Upload a new document", notes = "Upload a new document associated to the given element")
	@PostMapping(value = "/upload")
	public void upload(@RequestParam MultipartFile file, PuiDocumentLite document)
			throws PuiDocumentsUploadException, PuiServiceInsertException, PuiDocumentsThumbnailException,
			PuiDocumentsExtensionsException, PuiDocumentsFileSizeException {
		getService().upload(document);
	}

	@PuiFunctionality(id = ID_FUNCTIONALITY_GET, value = METHOD_FUNCTIONALITY_GET)
	@ApiOperation(value = "Download a document", notes = "Download the document associated with the given PK")
	@GetMapping(value = "/download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public FileDownload download(
			@ApiParam(value = "The PK of the existing document.", required = true) IPuiDocumentPk pk) {
		return getService().getFile(pk);
	}

	@PuiFunctionality(id = ID_FUNCTIONALITY_GET, value = METHOD_FUNCTIONALITY_GET)
	@ApiOperation(value = "Get all models with documents", notes = "Get all models with documents")
	@GetMapping(value = "/getModelsWithDocuments", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<String> getModelsWithDocumentsdownload() {
		return getService().getModelsWithDocuments();
	}

	@ApiOperation(value = "Force a reload of the document configuracions", notes = "Force a reload of the document configuracions")
	@GetMapping(value = "/reload")
	public void reload() {
		getService().reloadDocumentsInfo();
	}

	@PuiFunctionality(id = ID_FUNCTIONALITY_UPDATE, value = METHOD_FUNCTIONALITY_UPDATE)
	@ApiOperation(value = "Reload the thumbnails of ALL the images (executed in parallel thread)", notes = "Reload the thumbnails of ALL the images (executed in parallel thread)")
	@GetMapping(value = "/reloadThumbnails")
	public void reloadThumbnails() {
		getService().reloadThumbnails();
	}

}
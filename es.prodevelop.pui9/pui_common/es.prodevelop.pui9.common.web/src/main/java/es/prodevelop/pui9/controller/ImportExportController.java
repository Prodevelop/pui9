package es.prodevelop.pui9.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import es.prodevelop.pui9.annotations.PuiFunctionality;
import es.prodevelop.pui9.common.exceptions.PuiCommonImportExportInvalidColumnException;
import es.prodevelop.pui9.common.exceptions.PuiCommonImportExportInvalidModelException;
import es.prodevelop.pui9.common.exceptions.PuiCommonImportExportNoModelException;
import es.prodevelop.pui9.common.exceptions.PuiCommonImportExportPkNotIncludedException;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiImportexportPk;
import es.prodevelop.pui9.exceptions.PuiServiceException;
import es.prodevelop.pui9.exceptions.PuiServiceInsertException;
import es.prodevelop.pui9.file.FileDownload;
import es.prodevelop.pui9.importexport.ImportData;
import es.prodevelop.pui9.importexport.PuiImportExportAction;
import es.prodevelop.pui9.search.ExportColumnDefinition;
import es.prodevelop.pui9.search.ExportRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * This controller allows to export all the data of a grid and import data for
 * updating the records
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
@Controller
@Api(tags = "PUI Import Export")
@RequestMapping("/importexport")
public class ImportExportController extends AbstractPuiController {

	public static final String EXECUTE_FUNCTIONALITY = "EXECUTE_IMPORT_EXPORT";

	@Autowired
	private PuiImportExportAction puiImportExportAction;

	@PuiFunctionality(id = EXECUTE_FUNCTIONALITY, value = EXECUTE_FUNCTIONALITY)
	@ApiOperation(value = "Get the exportable columns for the grid", notes = "Get the exportable columns for the grid")
	@GetMapping(value = "/getExportableColumns", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<ExportColumnDefinition> getExportableColumns(@RequestParam String model)
			throws PuiCommonImportExportInvalidModelException {
		return puiImportExportAction.getExportUtil().getExportableColumns(model);
	}

	@PuiFunctionality(id = EXECUTE_FUNCTIONALITY, value = EXECUTE_FUNCTIONALITY)
	@ApiOperation(value = "Export the grid for future Import", notes = "Export the grid for future Import")
	@PostMapping(value = "/export", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public FileDownload export(@RequestBody ExportRequest req)
			throws PuiCommonImportExportNoModelException, PuiCommonImportExportPkNotIncludedException,
			PuiCommonImportExportInvalidColumnException, PuiCommonImportExportInvalidModelException {
		return puiImportExportAction.getExportUtil().export(req);
	}

	@PuiFunctionality(id = EXECUTE_FUNCTIONALITY, value = EXECUTE_FUNCTIONALITY)
	@ApiOperation(value = "Prepare the import", notes = "Prepare the import")
	@PostMapping(value = "/prepareImport", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ImportData prepareImport(@RequestParam String model, @RequestParam MultipartFile csv) throws IOException,
			PuiCommonImportExportPkNotIncludedException, PuiCommonImportExportInvalidColumnException,
			PuiCommonImportExportInvalidModelException, PuiServiceInsertException {
		return puiImportExportAction.getImportUtil().prepareImport(model, csv.getInputStream());
	}

	@PuiFunctionality(id = EXECUTE_FUNCTIONALITY, value = EXECUTE_FUNCTIONALITY)
	@ApiOperation(value = "Perform the import", notes = "Perform the import")
	@GetMapping(value = "/performImport")
	public void performImport(IPuiImportexportPk pk) throws PuiServiceException {
		puiImportExportAction.getImportUtil().performImport(pk);
	}

	@PuiFunctionality(id = EXECUTE_FUNCTIONALITY, value = EXECUTE_FUNCTIONALITY)
	@ApiOperation(value = "Cancel the import", notes = "Cancel the import")
	@GetMapping(value = "/cancelImport")
	public void cancelImport(IPuiImportexportPk pk) throws PuiServiceException {
		puiImportExportAction.getImportUtil().cancelImport(pk);
	}

	/**
	 * Reload the cache of models with import/export action from the database
	 */
	@ApiOperation(value = "Force a reload of all the models with import/export action", notes = "Force a reload of all the models with import/export action")
	@GetMapping(value = "/reload")
	public void reload() {
		puiImportExportAction.reloadModels(true);
	}

}

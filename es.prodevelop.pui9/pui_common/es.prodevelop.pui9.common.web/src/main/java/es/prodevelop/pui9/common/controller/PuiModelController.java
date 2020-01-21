package es.prodevelop.pui9.common.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import es.prodevelop.pui9.common.model.dao.interfaces.IPuiModelDao;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiModel;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiModelPk;
import es.prodevelop.pui9.common.service.interfaces.IPuiModelService;
import es.prodevelop.pui9.controller.AbstractCommonController;
import es.prodevelop.pui9.exceptions.PuiServiceGetException;
import es.prodevelop.pui9.model.configuration.PuiModelConfiguration;
import es.prodevelop.pui9.model.dao.interfaces.INullViewDao;
import es.prodevelop.pui9.model.dto.interfaces.INullView;
import es.prodevelop.pui9.search.SearchRequest;
import es.prodevelop.pui9.search.SearchResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * This controller allows to retrieve the information about the models of the
 * application (views structure)
 * 
 * @generated
 * @author Marc Gil - mgil@prodevelop.es
 */
@Controller
@Api(tags = "PUI Model")
@RequestMapping("/puimodel")
public class PuiModelController extends
		AbstractCommonController<IPuiModelPk, IPuiModel, INullView, IPuiModelDao, INullViewDao, IPuiModelService> {

	@Override
	public boolean allowDelete() {
		return false;
	}

	@Override
	public boolean allowGet() {
		return false;
	}

	@Override
	public boolean allowInsert() {
		return false;
	}

	@Override
	public boolean allowUpdate() {
		return false;
	}

	/**
	 * Executes a complex loup search
	 * 
	 * @param req The configuration of the search
	 * @return The list of registries that accomplish the conditions of the request
	 * @throws PuiServiceGetException If an error is throws in the search
	 */
	@ApiOperation(value = "Generic view search (loupe)", notes = "List all the elements that accomplish the given condition")
	@PostMapping(value = "/search", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public <TYPE> SearchResponse<TYPE> search(
			@ApiParam(value = "The information for the search", required = true) @RequestBody SearchRequest req)
			throws PuiServiceGetException {
		return getService().search(req);
	}

	/**
	 * Reload the cache of models from the database
	 */
	@ApiOperation(value = "Force a reload of all the models", notes = "Force a reload of all the models")
	@GetMapping(value = "/reload")
	public void reload() {
		getService().reloadModels(true);
	}

	/**
	 * Get all the model configuration customized for the logged user, with all of
	 * the grid filters and configurations
	 * 
	 * @return A Map with all of the filters and configurations of the models for
	 *         logged user
	 */
	@ApiOperation(value = "All the model configuration", notes = "Get the configuration of all the models")
	@GetMapping(value = "/getModelConfigurations", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, PuiModelConfiguration> getModelConfigurations() {
		return getService().getPuiModelConfigurations();
	}

	/**
	 * Get all the models available on the application
	 * 
	 * @return A List with all of the models
	 */
	@ApiOperation(value = "All the models", notes = "Get all the models")
	@GetMapping(value = "/getAllModels", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<String> getAllModels() {
		return getService().getAllModels();
	}

}
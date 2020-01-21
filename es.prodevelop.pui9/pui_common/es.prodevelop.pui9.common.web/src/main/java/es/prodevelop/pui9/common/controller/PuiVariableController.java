package es.prodevelop.pui9.common.controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import es.prodevelop.pui9.annotations.PuiNoSessionRequired;
import es.prodevelop.pui9.common.model.dao.interfaces.IPuiVariableDao;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiVariable;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiVariablePk;
import es.prodevelop.pui9.common.model.views.dao.interfaces.IVPuiVariableDao;
import es.prodevelop.pui9.common.model.views.dto.interfaces.IVPuiVariable;
import es.prodevelop.pui9.common.service.interfaces.IPuiVariableService;
import es.prodevelop.pui9.controller.AbstractCommonController;
import es.prodevelop.pui9.exceptions.PuiServiceGetException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * This controller allows to manage the variables of the application
 * 
 * @generated
 * @author Marc Gil - mgil@prodevelop.es
 */
@Controller
@Api(tags = "PUI Variable")
@RequestMapping("/puivariable")
public class PuiVariableController extends
		AbstractCommonController<IPuiVariablePk, IPuiVariable, IVPuiVariable, IPuiVariableDao, IVPuiVariableDao, IPuiVariableService> {

	/**
	 * @generated
	 */
	@Override
	protected String getReadFunctionality() {
		return "READ_PUI_VARIABLE";
	}

	/**
	 * @generated
	 */
	@Override
	protected String getWriteFunctionality() {
		return "WRITE_PUI_VARIABLE";
	}

	@Override
	public boolean allowInsert() {
		return false;
	}

	@Override
	public boolean allowDelete() {
		return false;
	}

	/**
	 * Obtain the list of variables of the application. No session is required to
	 * execute this method
	 * 
	 * @return The list of variables of the application
	 * @throws PuiServiceGetException If an error while retrieving the variables was
	 *                                throws
	 */
	@ApiOperation(value = "Get all the variables", notes = "Get all the variables.")
	@GetMapping(value = "/getAllVariables", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<IPuiVariable> getAllVariables() throws PuiServiceGetException {
		return getService().getAll();
	}

	/**
	 * Get the value of a variable
	 * 
	 * @param variable The name of the variable to retrieve
	 * @return The value of the given variable
	 */
	@ApiOperation(value = "Get the value of this variable", notes = "Get the value of the given variable as String value.")
	@GetMapping(value = "/getVariable/{variable}", produces = MediaType.APPLICATION_JSON_VALUE)
	public String getVariable(
			@ApiParam(value = "The name of the variable to get", required = true) @PathVariable String variable) {
		return getService().getVariable(variable);
	}

	/**
	 * Reload the cache of the variables from the database
	 */
	@ApiOperation(value = "Force a reload of all the variables", notes = "Force a reload of all the variables")
	@GetMapping(value = "/reload")
	public void reload() {
		getService().reloadVariables();
	}

	/**
	 * Get the Application Legal Text
	 */
	@ApiOperation(value = "Get the Application Legal Text", notes = "Get the Application Legal Text")
	@GetMapping(value = "/getApplicationLegalText", produces = MediaType.APPLICATION_JSON_VALUE)
	public String getApplicationLegalText() {
		return getService().getApplicationLegalText();
	}

	/**
	 * Check if the environment if for development
	 */
	@PuiNoSessionRequired
	@ApiOperation(value = "Check if the environment if for development", notes = "Check if the environment if for development")
	@GetMapping(value = "/isDevelopmentEnvironment", produces = MediaType.APPLICATION_JSON_VALUE)
	public Boolean isDevelopmentEnvironment() {
		return getService().isDevelopmentEnvironment();
	}

}
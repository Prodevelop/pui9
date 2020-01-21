package es.prodevelop.pui9.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.prodevelop.pui9.annotations.PuiFunctionality;
import es.prodevelop.pui9.common.exceptions.PuiCommonCopyInvalidModelException;
import es.prodevelop.pui9.copy.PuiCopyAction;
import es.prodevelop.pui9.exceptions.PuiServiceCopyRegistryException;
import es.prodevelop.pui9.model.dto.interfaces.ITableDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * This controller allows to export all the data of a grid and import data for
 * updating the records
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
@Controller
@Api(tags = "PUI Copy")
@RequestMapping("/copy")
public class CopyController extends AbstractPuiController {

	public static final String EXECUTE_FUNCTIONALITY = "EXECUTE_COPY";

	@Autowired
	private PuiCopyAction puiCopyAction;

	@PuiFunctionality(id = EXECUTE_FUNCTIONALITY, value = EXECUTE_FUNCTIONALITY)
	@ApiOperation(value = "Copy the registry represented by the given PK", notes = "Copy the registry represented by the given PK")
	@PostMapping(value = "/perform", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public <T extends ITableDto> T perform(@RequestParam String model, @RequestBody Map<String, Object> pk)
			throws PuiServiceCopyRegistryException, PuiCommonCopyInvalidModelException {
		return puiCopyAction.performCopy(model, pk);
	}

	/**
	 * Reload the cache of models with copy action from the database
	 */
	@ApiOperation(value = "Force a reload of all the models with copy action", notes = "Force a reload of all the models with copy action")
	@GetMapping(value = "/reload")
	public void reload() {
		puiCopyAction.reloadModels(true);
	}

}

package es.prodevelop.pui9.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import es.prodevelop.pui9.annotations.PuiNoSessionRequired;
import es.prodevelop.pui9.components.PuiApplicationContext;
import es.prodevelop.pui9.model.dao.interfaces.IDao;
import es.prodevelop.pui9.model.dao.registry.DaoRegistry;
import es.prodevelop.pui9.model.dto.interfaces.IDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * This controller allows to list all the DTOs available in the application and
 * define all of them, giving information of all of its attributes
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
@Controller
@PuiNoSessionRequired
@Api(tags = "PUI DTOs")
@RequestMapping("/dtos")
public class PuiDtoController extends AbstractPuiController {

	@Autowired
	private PuiApplicationContext context;

	@Autowired
	protected DaoRegistry daoRegistry;

	/**
	 * List all the available DTOs registered in the application
	 * 
	 * @return The list of IDs of the DTOs
	 */
	@ApiOperation(value = "List all DTO", notes = "List all available DTO identifiers. Use one of them to decribe it")
	@GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<String> list() {
		return daoRegistry.getAllDaoModelId();
	}

	/**
	 * Define the given DTO
	 * 
	 * @param id The name of the DTO (the {@link #list()} method will retrieve all
	 *           the DTO names)
	 * @return The definition of the DTO
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ApiOperation(value = "Define a DTO", notes = "Get the full definition of a DTO with the given identifier, including the type of the attributes and some useful information.")
	@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Class<? extends IDto> define(
			@ApiParam(value = "The identifier name of the DTO", required = true) @PathVariable String id) {
		Class<? extends IDao> daoClass = daoRegistry.getDaoFromModelId(id.toLowerCase());
		Assert.notNull(daoClass, "A DTO class should have a related DAO class");
		return context.getBean(daoClass).getDtoClass();
	}
}

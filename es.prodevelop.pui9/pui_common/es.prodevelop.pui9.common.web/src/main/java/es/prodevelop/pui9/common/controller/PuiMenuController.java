package es.prodevelop.pui9.common.controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import es.prodevelop.pui9.common.model.dao.interfaces.IPuiMenuDao;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiMenu;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiMenuPk;
import es.prodevelop.pui9.common.service.interfaces.IPuiMenuService;
import es.prodevelop.pui9.controller.AbstractCommonController;
import es.prodevelop.pui9.model.dao.interfaces.INullViewDao;
import es.prodevelop.pui9.model.dto.interfaces.INullView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * This controller allows to retrieve the menu of the application of the logged
 * user
 * 
 * @generated
 * @author Marc Gil - mgil@prodevelop.es
 */
@Controller
@Api(tags = "PUI Menu")
@RequestMapping("/puimenu")
public class PuiMenuController
		extends AbstractCommonController<IPuiMenuPk, IPuiMenu, INullView, IPuiMenuDao, INullViewDao, IPuiMenuService> {

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
	 * Retrieves the whole application menu for the logged user
	 * 
	 * @return The menu for the logged user
	 */
	@ApiOperation(value = "Get the menu", notes = "Build and return the menu of the application, depending on the functionalities the logger user has")
	@GetMapping(value = "/getMenu", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<IPuiMenu> getMenu() {
		return getService().getMenuForLoggerUser();
	}

}
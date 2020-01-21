package es.prodevelop.pui9.common.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import es.prodevelop.pui9.common.model.dao.interfaces.IPuiProfileDao;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiProfile;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiProfilePk;
import es.prodevelop.pui9.common.model.views.dao.interfaces.IVPuiProfileDao;
import es.prodevelop.pui9.common.model.views.dto.interfaces.IVPuiProfile;
import es.prodevelop.pui9.common.service.interfaces.IPuiProfileService;
import es.prodevelop.pui9.controller.AbstractCommonController;
import io.swagger.annotations.Api;

/**
 * This controller allows to manage the profiles of the application
 * 
 * @generated
 * @author Marc Gil - mgil@prodevelop.es
 */
@Controller
@Api(tags = "PUI Profile")
@RequestMapping("/puiprofile")
public class PuiProfileController extends
		AbstractCommonController<IPuiProfilePk, IPuiProfile, IVPuiProfile, IPuiProfileDao, IVPuiProfileDao, IPuiProfileService> {
	/**
	 * @generated
	 */
	@Override
	protected String getReadFunctionality() {
		return "READ_PUI_PROFILE";
	}

	/**
	 * @generated
	 */
	@Override
	protected String getWriteFunctionality() {
		return "WRITE_PUI_PROFILE";
	}

}
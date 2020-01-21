package es.prodevelop.pui9.common.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import es.prodevelop.pui9.common.model.dao.interfaces.IPuiUserModelFilterDao;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiUserModelFilter;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiUserModelFilterPk;
import es.prodevelop.pui9.common.service.interfaces.IPuiUserModelFilterService;
import es.prodevelop.pui9.controller.AbstractCommonController;
import es.prodevelop.pui9.model.dao.interfaces.INullViewDao;
import es.prodevelop.pui9.model.dto.interfaces.INullView;
import io.swagger.annotations.Api;

/**
 * This controller is used to manage the model filters of the users
 * 
 * @generated
 * @author Marc Gil - mgil@prodevelop.es
 */
@Controller
@Api(tags = "PUI User Model Filter")
@RequestMapping("/puiusermodelfilter")
public class PuiUserModelFilterController extends
		AbstractCommonController<IPuiUserModelFilterPk, IPuiUserModelFilter, INullView, IPuiUserModelFilterDao, INullViewDao, IPuiUserModelFilterService> {

	@Override
	public boolean allowGet() {
		return false;
	}

}
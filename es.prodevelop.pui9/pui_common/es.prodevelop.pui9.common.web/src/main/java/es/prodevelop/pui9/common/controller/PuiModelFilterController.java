package es.prodevelop.pui9.common.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import es.prodevelop.pui9.common.model.dao.interfaces.IPuiModelFilterDao;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiModelFilter;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiModelFilterPk;
import es.prodevelop.pui9.common.service.interfaces.IPuiModelFilterService;
import es.prodevelop.pui9.controller.AbstractCommonController;
import es.prodevelop.pui9.model.dao.interfaces.INullViewDao;
import es.prodevelop.pui9.model.dto.interfaces.INullView;
import io.swagger.annotations.Api;

/**
 * This controller allows to list the application filters of the models, but all
 * of them are readonly
 * 
 * @generated
 * @author Marc Gil - mgil@prodevelop.es
 */
@Controller
@Api(tags = "PUI Model Filter")
@RequestMapping("/puimodelfilter")
public class PuiModelFilterController extends
		AbstractCommonController<IPuiModelFilterPk, IPuiModelFilter, INullView, IPuiModelFilterDao, INullViewDao, IPuiModelFilterService> {

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

}
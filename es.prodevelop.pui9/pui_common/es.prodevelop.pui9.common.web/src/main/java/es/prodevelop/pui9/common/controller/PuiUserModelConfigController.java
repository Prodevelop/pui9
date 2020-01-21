package es.prodevelop.pui9.common.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import es.prodevelop.pui9.common.model.dao.interfaces.IPuiUserModelConfigDao;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiUserModelConfig;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiUserModelConfigPk;
import es.prodevelop.pui9.common.service.interfaces.IPuiUserModelConfigService;
import es.prodevelop.pui9.controller.AbstractCommonController;
import es.prodevelop.pui9.model.dao.interfaces.INullViewDao;
import es.prodevelop.pui9.model.dto.interfaces.INullView;
import io.swagger.annotations.Api;

/**
 * A controller to manage the configuration of the models. Basically this
 * configuration is about the grid
 * 
 * @generated
 * @author Marc Gil - mgil@prodevelop.es
 */
@Controller
@Api(tags = "PUI User Model Config")
@RequestMapping("/puiusermodelconfig")
public class PuiUserModelConfigController extends
		AbstractCommonController<IPuiUserModelConfigPk, IPuiUserModelConfig, INullView, IPuiUserModelConfigDao, INullViewDao, IPuiUserModelConfigService> {

	@Override
	public boolean allowGet() {
		return false;
	}

}
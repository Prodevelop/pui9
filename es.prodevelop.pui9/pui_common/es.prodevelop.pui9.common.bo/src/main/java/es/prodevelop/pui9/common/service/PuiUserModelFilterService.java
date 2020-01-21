package es.prodevelop.pui9.common.service;

import org.springframework.stereotype.Service;

import es.prodevelop.pui9.common.model.dao.interfaces.IPuiUserModelFilterDao;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiUserModelFilter;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiUserModelFilterPk;
import es.prodevelop.pui9.common.service.interfaces.IPuiUserModelFilterService;
import es.prodevelop.pui9.model.dao.interfaces.INullViewDao;
import es.prodevelop.pui9.model.dto.interfaces.INullView;
import es.prodevelop.pui9.service.AbstractService;

/**
 * @generated
 */
@Service
public class PuiUserModelFilterService extends
		AbstractService<IPuiUserModelFilterPk, IPuiUserModelFilter, INullView, IPuiUserModelFilterDao, INullViewDao>
		implements IPuiUserModelFilterService {

}
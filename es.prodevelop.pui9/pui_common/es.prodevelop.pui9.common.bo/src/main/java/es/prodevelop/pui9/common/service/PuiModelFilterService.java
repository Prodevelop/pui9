package es.prodevelop.pui9.common.service;

import org.springframework.stereotype.Service;

import es.prodevelop.pui9.common.model.dao.interfaces.IPuiModelFilterDao;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiModelFilter;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiModelFilterPk;
import es.prodevelop.pui9.common.service.interfaces.IPuiModelFilterService;
import es.prodevelop.pui9.model.dao.interfaces.INullViewDao;
import es.prodevelop.pui9.model.dto.interfaces.INullView;
import es.prodevelop.pui9.service.AbstractService;

/**
 * @generated
 */
@Service
public class PuiModelFilterService
		extends AbstractService<IPuiModelFilterPk, IPuiModelFilter, INullView, IPuiModelFilterDao, INullViewDao>
		implements IPuiModelFilterService {

}
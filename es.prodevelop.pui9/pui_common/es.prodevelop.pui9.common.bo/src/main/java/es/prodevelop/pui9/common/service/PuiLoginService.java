package es.prodevelop.pui9.common.service;

import org.springframework.stereotype.Service;

import es.prodevelop.pui9.common.model.views.dao.interfaces.IVPuiLoginDao;
import es.prodevelop.pui9.common.model.views.dto.interfaces.IVPuiLogin;
import es.prodevelop.pui9.common.service.interfaces.IPuiLoginService;
import es.prodevelop.pui9.model.dao.interfaces.INullTableDao;
import es.prodevelop.pui9.model.dto.interfaces.INullTable;
import es.prodevelop.pui9.model.dto.interfaces.INullTablePk;
import es.prodevelop.pui9.service.AbstractService;

/**
 * @generated
 */
@Service
public class PuiLoginService
		extends AbstractService<INullTablePk, INullTable, IVPuiLogin, INullTableDao, IVPuiLoginDao>
		implements IPuiLoginService {
}

package es.prodevelop.pui9.common.service;

import org.springframework.stereotype.Service;

import es.prodevelop.pui9.common.model.dao.interfaces.IPuiAuditDao;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiAudit;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiAuditPk;
import es.prodevelop.pui9.common.model.views.dao.interfaces.IVPuiAuditDao;
import es.prodevelop.pui9.common.model.views.dto.interfaces.IVPuiAudit;
import es.prodevelop.pui9.common.service.interfaces.IPuiAuditService;
import es.prodevelop.pui9.service.AbstractService;

/**
 * @generated
 */
@Service
public class PuiAuditService extends AbstractService<IPuiAuditPk, IPuiAudit, IVPuiAudit, IPuiAuditDao, IVPuiAuditDao>
		implements IPuiAuditService {
}
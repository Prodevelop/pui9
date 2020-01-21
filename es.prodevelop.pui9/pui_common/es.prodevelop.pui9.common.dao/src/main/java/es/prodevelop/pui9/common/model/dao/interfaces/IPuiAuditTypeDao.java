package es.prodevelop.pui9.common.model.dao.interfaces;

import es.prodevelop.pui9.common.model.dto.interfaces.IPuiAuditType;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiAuditTypePk;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.interfaces.ITableDao;

/**
 * @generated
 */
public interface IPuiAuditTypeDao extends ITableDao<IPuiAuditTypePk, IPuiAuditType> {
	/**
	 * @generated
	 */
	java.util.List<IPuiAuditType> findByType(String type) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IPuiAuditType> findByDescription(String description) throws PuiDaoFindException;
}

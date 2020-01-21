package es.prodevelop.pui9.common.model.dao;

import org.springframework.stereotype.Repository;

import es.prodevelop.pui9.common.model.dao.interfaces.IPuiAuditTypeDao;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiAuditType;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiAuditTypePk;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.AbstractTableDao;

/**
 * @generated
 */
@Repository
public class PuiAuditTypeDao extends AbstractTableDao<IPuiAuditTypePk, IPuiAuditType> implements IPuiAuditTypeDao {
	/**
	 * @generated
	 */
	@Override
	public java.util.List<IPuiAuditType> findByType(String type) throws PuiDaoFindException {
		return super.findByColumn(IPuiAuditTypePk.TYPE_FIELD, type);
	}

	/**
	 * @generated
	 */
	@Override
	public java.util.List<IPuiAuditType> findByDescription(String description) throws PuiDaoFindException {
		return super.findByColumn(IPuiAuditType.DESCRIPTION_FIELD, description);
	}
}

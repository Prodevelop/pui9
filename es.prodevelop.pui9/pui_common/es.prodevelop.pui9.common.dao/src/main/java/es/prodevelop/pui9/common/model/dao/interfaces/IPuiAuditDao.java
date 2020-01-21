package es.prodevelop.pui9.common.model.dao.interfaces;

import es.prodevelop.pui9.common.model.dto.interfaces.IPuiAudit;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiAuditPk;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.interfaces.ITableDao;

/**
 * @generated
 */
public interface IPuiAuditDao extends ITableDao<IPuiAuditPk, IPuiAudit> {
	/**
	 * @generated
	 */
	java.util.List<IPuiAudit> findById(Integer id) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IPuiAudit> findByModel(String model) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IPuiAudit> findByType(String type) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IPuiAudit> findByPk(String pk) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IPuiAudit> findByDatetime(java.time.Instant datetime) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IPuiAudit> findByUsr(String usr) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IPuiAudit> findByIp(String ip) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IPuiAudit> findByDto(String dto) throws PuiDaoFindException;
}

package es.prodevelop.pui9.common.model.views.dao.interfaces;

import es.prodevelop.pui9.common.model.views.dto.interfaces.IVPuiAudit;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.interfaces.IViewDao;

/**
 * @generated
 */
public interface IVPuiAuditDao extends IViewDao<IVPuiAudit> {
	/**
	 * @generated
	 */
	java.util.List<IVPuiAudit> findById(Integer id) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IVPuiAudit> findByDatetime(java.time.Instant datetime) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IVPuiAudit> findByUsr(String usr) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IVPuiAudit> findByIp(String ip) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IVPuiAudit> findByType(String type) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IVPuiAudit> findByModel(String model) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IVPuiAudit> findByPk(String pk) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IVPuiAudit> findByDto(String dto) throws PuiDaoFindException;
}

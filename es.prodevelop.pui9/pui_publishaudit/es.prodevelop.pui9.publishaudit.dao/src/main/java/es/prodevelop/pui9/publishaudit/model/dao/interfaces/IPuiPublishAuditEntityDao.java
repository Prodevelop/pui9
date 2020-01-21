package es.prodevelop.pui9.publishaudit.model.dao.interfaces;

import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.interfaces.ITableDao;
import es.prodevelop.pui9.publishaudit.model.dto.interfaces.IPuiPublishAuditEntity;
import es.prodevelop.pui9.publishaudit.model.dto.interfaces.IPuiPublishAuditEntityPk;

/**
 * @generated
 */
public interface IPuiPublishAuditEntityDao extends ITableDao<IPuiPublishAuditEntityPk, IPuiPublishAuditEntity> {
	/**
	 * @generated
	 */
	java.util.List<IPuiPublishAuditEntity> findById(Integer id) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IPuiPublishAuditEntity> findByEntity(String entity) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IPuiPublishAuditEntity> findByAuditinsert(Integer auditinsert) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IPuiPublishAuditEntity> findByAuditupdate(Integer auditupdate) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IPuiPublishAuditEntity> findByAuditdelete(Integer auditdelete) throws PuiDaoFindException;
}

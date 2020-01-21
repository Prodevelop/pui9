package es.prodevelop.pui9.publishaudit.model.dao.interfaces;

import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.interfaces.ITableDao;
import es.prodevelop.pui9.publishaudit.model.dto.interfaces.IPuiPublishAuditField;
import es.prodevelop.pui9.publishaudit.model.dto.interfaces.IPuiPublishAuditFieldPk;

/**
 * @generated
 */
public interface IPuiPublishAuditFieldDao extends ITableDao<IPuiPublishAuditFieldPk, IPuiPublishAuditField> {
	/**
	 * @generated
	 */
	java.util.List<IPuiPublishAuditField> findById(Integer id) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IPuiPublishAuditField> findByIdtopic(Integer idtopic) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IPuiPublishAuditField> findByField(String field) throws PuiDaoFindException;
}

package es.prodevelop.pui9.publishaudit.model.dao.interfaces;

import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.interfaces.ITableDao;
import es.prodevelop.pui9.publishaudit.model.dto.interfaces.IPuiPublishAuditFieldValue;
import es.prodevelop.pui9.publishaudit.model.dto.interfaces.IPuiPublishAuditFieldValuePk;

/**
 * @generated
 */
public interface IPuiPublishAuditFieldValueDao
		extends ITableDao<IPuiPublishAuditFieldValuePk, IPuiPublishAuditFieldValue> {
	/**
	 * @generated
	 */
	java.util.List<IPuiPublishAuditFieldValue> findById(Integer id) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IPuiPublishAuditFieldValue> findByIdfield(Integer idfield) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IPuiPublishAuditFieldValue> findByOldvalue(String oldvalue) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IPuiPublishAuditFieldValue> findByNewvalue(String newvalue) throws PuiDaoFindException;
}

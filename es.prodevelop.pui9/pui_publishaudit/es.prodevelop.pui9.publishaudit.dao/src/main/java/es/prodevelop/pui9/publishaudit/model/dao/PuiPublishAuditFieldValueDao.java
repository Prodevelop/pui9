package es.prodevelop.pui9.publishaudit.model.dao;

import org.springframework.stereotype.Repository;

import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.AbstractTableDao;
import es.prodevelop.pui9.publishaudit.model.dao.interfaces.IPuiPublishAuditFieldValueDao;
import es.prodevelop.pui9.publishaudit.model.dto.interfaces.IPuiPublishAuditFieldValue;
import es.prodevelop.pui9.publishaudit.model.dto.interfaces.IPuiPublishAuditFieldValuePk;

/**
 * @generated
 */
@Repository
public class PuiPublishAuditFieldValueDao
		extends AbstractTableDao<IPuiPublishAuditFieldValuePk, IPuiPublishAuditFieldValue>
		implements IPuiPublishAuditFieldValueDao {
	/**
	 * @generated
	 */
	@Override
	public java.util.List<IPuiPublishAuditFieldValue> findById(Integer id) throws PuiDaoFindException {
		return super.findByColumn(IPuiPublishAuditFieldValuePk.ID_FIELD, id);
	}

	/**
	 * @generated
	 */
	@Override
	public java.util.List<IPuiPublishAuditFieldValue> findByIdfield(Integer idfield) throws PuiDaoFindException {
		return super.findByColumn(IPuiPublishAuditFieldValue.ID_FIELD_FIELD, idfield);
	}

	/**
	 * @generated
	 */
	@Override
	public java.util.List<IPuiPublishAuditFieldValue> findByOldvalue(String oldvalue) throws PuiDaoFindException {
		return super.findByColumn(IPuiPublishAuditFieldValue.OLD_VALUE_FIELD, oldvalue);
	}

	/**
	 * @generated
	 */
	@Override
	public java.util.List<IPuiPublishAuditFieldValue> findByNewvalue(String newvalue) throws PuiDaoFindException {
		return super.findByColumn(IPuiPublishAuditFieldValue.NEW_VALUE_FIELD, newvalue);
	}
}

package es.prodevelop.pui9.publishaudit.model.dao;

import org.springframework.stereotype.Repository;

import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.AbstractTableDao;
import es.prodevelop.pui9.publishaudit.model.dao.interfaces.IPuiPublishAuditFieldDao;
import es.prodevelop.pui9.publishaudit.model.dto.interfaces.IPuiPublishAuditField;
import es.prodevelop.pui9.publishaudit.model.dto.interfaces.IPuiPublishAuditFieldPk;

/**
 * @generated
 */
@Repository
public class PuiPublishAuditFieldDao extends AbstractTableDao<IPuiPublishAuditFieldPk, IPuiPublishAuditField>
		implements IPuiPublishAuditFieldDao {
	/**
	 * @generated
	 */
	@Override
	public java.util.List<IPuiPublishAuditField> findById(Integer id) throws PuiDaoFindException {
		return super.findByColumn(IPuiPublishAuditFieldPk.ID_FIELD, id);
	}

	/**
	 * @generated
	 */
	@Override
	public java.util.List<IPuiPublishAuditField> findByIdtopic(Integer idtopic) throws PuiDaoFindException {
		return super.findByColumn(IPuiPublishAuditField.ID_TOPIC_FIELD, idtopic);
	}

	/**
	 * @generated
	 */
	@Override
	public java.util.List<IPuiPublishAuditField> findByField(String field) throws PuiDaoFindException {
		return super.findByColumn(IPuiPublishAuditField.FIELD_FIELD, field);
	}
}

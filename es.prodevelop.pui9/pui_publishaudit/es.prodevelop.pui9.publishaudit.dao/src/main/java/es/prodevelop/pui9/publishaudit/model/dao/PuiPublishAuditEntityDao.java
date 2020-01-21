package es.prodevelop.pui9.publishaudit.model.dao;

import org.springframework.stereotype.Repository;

import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.AbstractTableDao;
import es.prodevelop.pui9.publishaudit.model.dao.interfaces.IPuiPublishAuditEntityDao;
import es.prodevelop.pui9.publishaudit.model.dto.interfaces.IPuiPublishAuditEntity;
import es.prodevelop.pui9.publishaudit.model.dto.interfaces.IPuiPublishAuditEntityPk;

/**
 * @generated
 */
@Repository
public class PuiPublishAuditEntityDao extends AbstractTableDao<IPuiPublishAuditEntityPk, IPuiPublishAuditEntity>
		implements IPuiPublishAuditEntityDao {
	/**
	 * @generated
	 */
	@Override
	public java.util.List<IPuiPublishAuditEntity> findById(Integer id) throws PuiDaoFindException {
		return super.findByColumn(IPuiPublishAuditEntityPk.ID_FIELD, id);
	}

	/**
	 * @generated
	 */
	@Override
	public java.util.List<IPuiPublishAuditEntity> findByEntity(String entity) throws PuiDaoFindException {
		return super.findByColumn(IPuiPublishAuditEntity.ENTITY_FIELD, entity);
	}

	/**
	 * @generated
	 */
	@Override
	public java.util.List<IPuiPublishAuditEntity> findByAuditinsert(Integer auditinsert) throws PuiDaoFindException {
		return super.findByColumn(IPuiPublishAuditEntity.AUDIT_INSERT_FIELD, auditinsert);
	}

	/**
	 * @generated
	 */
	@Override
	public java.util.List<IPuiPublishAuditEntity> findByAuditupdate(Integer auditupdate) throws PuiDaoFindException {
		return super.findByColumn(IPuiPublishAuditEntity.AUDIT_UPDATE_FIELD, auditupdate);
	}

	/**
	 * @generated
	 */
	@Override
	public java.util.List<IPuiPublishAuditEntity> findByAuditdelete(Integer auditdelete) throws PuiDaoFindException {
		return super.findByColumn(IPuiPublishAuditEntity.AUDIT_DELETE_FIELD, auditdelete);
	}
}

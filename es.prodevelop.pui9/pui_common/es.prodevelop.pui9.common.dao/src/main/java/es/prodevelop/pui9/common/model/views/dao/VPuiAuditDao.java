package es.prodevelop.pui9.common.model.views.dao;

import org.springframework.stereotype.Repository;

import es.prodevelop.pui9.common.model.views.dao.interfaces.IVPuiAuditDao;
import es.prodevelop.pui9.common.model.views.dto.interfaces.IVPuiAudit;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.AbstractViewDao;

/**
 * @generated
 */
@Repository
public class VPuiAuditDao extends AbstractViewDao<IVPuiAudit> implements IVPuiAuditDao {
	/**
	 * @generated
	 */
	@Override
	public java.util.List<IVPuiAudit> findById(Integer id) throws PuiDaoFindException {
		return super.findByColumn(IVPuiAudit.ID_FIELD, id);
	}

	/**
	 * @generated
	 */
	@Override
	public java.util.List<IVPuiAudit> findByDatetime(java.time.Instant datetime) throws PuiDaoFindException {
		return super.findByColumn(IVPuiAudit.DATETIME_FIELD, datetime);
	}

	/**
	 * @generated
	 */
	@Override
	public java.util.List<IVPuiAudit> findByUsr(String usr) throws PuiDaoFindException {
		return super.findByColumn(IVPuiAudit.USR_FIELD, usr);
	}

	/**
	 * @generated
	 */
	@Override
	public java.util.List<IVPuiAudit> findByIp(String ip) throws PuiDaoFindException {
		return super.findByColumn(IVPuiAudit.IP_FIELD, ip);
	}

	/**
	 * @generated
	 */
	@Override
	public java.util.List<IVPuiAudit> findByType(String type) throws PuiDaoFindException {
		return super.findByColumn(IVPuiAudit.TYPE_FIELD, type);
	}

	/**
	 * @generated
	 */
	@Override
	public java.util.List<IVPuiAudit> findByModel(String model) throws PuiDaoFindException {
		return super.findByColumn(IVPuiAudit.MODEL_FIELD, model);
	}

	/**
	 * @generated
	 */
	@Override
	public java.util.List<IVPuiAudit> findByPk(String pk) throws PuiDaoFindException {
		return super.findByColumn(IVPuiAudit.PK_FIELD, pk);
	}

	/**
	 * @generated
	 */
	@Override
	public java.util.List<IVPuiAudit> findByDto(String dto) throws PuiDaoFindException {
		return super.findByColumn(IVPuiAudit.DTO_FIELD, dto);
	}
}

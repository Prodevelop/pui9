package es.prodevelop.pui9.common.model.dao;

import org.springframework.stereotype.Repository;

import es.prodevelop.pui9.common.model.dao.interfaces.IPuiAuditDao;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiAudit;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiAuditPk;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.AbstractTableDao;

/**
 * @generated
 */
@Repository
public class PuiAuditDao extends AbstractTableDao<IPuiAuditPk, IPuiAudit> implements IPuiAuditDao {
	/**
	 * @generated
	 */
	@Override
	public java.util.List<IPuiAudit> findById(Integer id) throws PuiDaoFindException {
		return super.findByColumn(IPuiAuditPk.ID_FIELD, id);
	}

	/**
	 * @generated
	 */
	@Override
	public java.util.List<IPuiAudit> findByModel(String model) throws PuiDaoFindException {
		return super.findByColumn(IPuiAudit.MODEL_FIELD, model);
	}

	/**
	 * @generated
	 */
	@Override
	public java.util.List<IPuiAudit> findByType(String type) throws PuiDaoFindException {
		return super.findByColumn(IPuiAudit.TYPE_FIELD, type);
	}

	/**
	 * @generated
	 */
	@Override
	public java.util.List<IPuiAudit> findByPk(String pk) throws PuiDaoFindException {
		return super.findByColumn(IPuiAudit.PK_FIELD, pk);
	}

	/**
	 * @generated
	 */
	@Override
	public java.util.List<IPuiAudit> findByDatetime(java.time.Instant datetime) throws PuiDaoFindException {
		return super.findByColumn(IPuiAudit.DATETIME_FIELD, datetime);
	}

	/**
	 * @generated
	 */
	@Override
	public java.util.List<IPuiAudit> findByUsr(String usr) throws PuiDaoFindException {
		return super.findByColumn(IPuiAudit.USR_FIELD, usr);
	}

	/**
	 * @generated
	 */
	@Override
	public java.util.List<IPuiAudit> findByIp(String ip) throws PuiDaoFindException {
		return super.findByColumn(IPuiAudit.IP_FIELD, ip);
	}

	/**
	 * @generated
	 */
	@Override
	public java.util.List<IPuiAudit> findByDto(String dto) throws PuiDaoFindException {
		return super.findByColumn(IPuiAudit.DTO_FIELD, dto);
	}
}

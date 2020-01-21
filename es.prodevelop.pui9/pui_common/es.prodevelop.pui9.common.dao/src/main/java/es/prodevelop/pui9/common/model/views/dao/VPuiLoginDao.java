package es.prodevelop.pui9.common.model.views.dao;

import org.springframework.stereotype.Repository;

import es.prodevelop.pui9.common.model.views.dao.interfaces.IVPuiLoginDao;
import es.prodevelop.pui9.common.model.views.dto.interfaces.IVPuiLogin;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.AbstractViewDao;

/**
 * @generated
 */
@Repository
public class VPuiLoginDao extends AbstractViewDao<IVPuiLogin> implements IVPuiLoginDao {
	/**
	 * @generated
	 */
	@Override
	public java.util.List<IVPuiLogin> findById(Integer id) throws PuiDaoFindException {
		return super.findByColumn(IVPuiLogin.ID_FIELD, id);
	}

	/**
	 * @generated
	 */
	@Override
	public java.util.List<IVPuiLogin> findByUsr(String usr) throws PuiDaoFindException {
		return super.findByColumn(IVPuiLogin.USR_FIELD, usr);
	}

	/**
	 * @generated
	 */
	@Override
	public java.util.List<IVPuiLogin> findByDatetime(java.time.Instant datetime) throws PuiDaoFindException {
		return super.findByColumn(IVPuiLogin.DATETIME_FIELD, datetime);
	}

	/**
	 * @generated
	 */
	@Override
	public java.util.List<IVPuiLogin> findByIp(String ip) throws PuiDaoFindException {
		return super.findByColumn(IVPuiLogin.IP_FIELD, ip);
	}
}

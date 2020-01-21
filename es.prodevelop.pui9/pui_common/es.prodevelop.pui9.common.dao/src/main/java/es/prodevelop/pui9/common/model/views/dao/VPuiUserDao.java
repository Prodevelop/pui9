package es.prodevelop.pui9.common.model.views.dao;

import org.springframework.stereotype.Repository;

import es.prodevelop.pui9.common.model.views.dao.interfaces.IVPuiUserDao;
import es.prodevelop.pui9.common.model.views.dto.interfaces.IVPuiUser;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.AbstractViewDao;

/**
 * @generated
 */
@Repository
public class VPuiUserDao extends AbstractViewDao<IVPuiUser> implements IVPuiUserDao {
	/**
	 * @generated
	 */
	@Override
	public java.util.List<IVPuiUser> findByUsr(String usr) throws PuiDaoFindException {
		return super.findByColumn(IVPuiUser.USR_FIELD, usr);
	}

	/**
	 * @generated
	 */
	@Override
	public java.util.List<IVPuiUser> findByName(String name) throws PuiDaoFindException {
		return super.findByColumn(IVPuiUser.NAME_FIELD, name);
	}

	/**
	 * @generated
	 */
	@Override
	public java.util.List<IVPuiUser> findByEmail(String email) throws PuiDaoFindException {
		return super.findByColumn(IVPuiUser.EMAIL_FIELD, email);
	}

	/**
	 * @generated
	 */
	@Override
	public java.util.List<IVPuiUser> findByLanguage(String language) throws PuiDaoFindException {
		return super.findByColumn(IVPuiUser.LANGUAGE_FIELD, language);
	}

	/**
	 * @generated
	 */
	@Override
	public java.util.List<IVPuiUser> findByDateformat(String dateformat) throws PuiDaoFindException {
		return super.findByColumn(IVPuiUser.DATEFORMAT_FIELD, dateformat);
	}

	/**
	 * @generated
	 */
	@Override
	public java.util.List<IVPuiUser> findByDisabled(Integer disabled) throws PuiDaoFindException {
		return super.findByColumn(IVPuiUser.DISABLED_FIELD, disabled);
	}

	/**
	 * @generated
	 */
	@Override
	public java.util.List<IVPuiUser> findByDisableddate(java.time.Instant disableddate) throws PuiDaoFindException {
		return super.findByColumn(IVPuiUser.DISABLED_DATE_FIELD, disableddate);
	}

	/**
	 * @generated
	 */
	@Override
	public java.util.List<IVPuiUser> findByLastaccesstime(java.time.Instant lastaccesstime) throws PuiDaoFindException {
		return super.findByColumn(IVPuiUser.LAST_ACCESS_TIME_FIELD, lastaccesstime);
	}

	/**
	 * @generated
	 */
	@Override
	public java.util.List<IVPuiUser> findByLastaccessip(String lastaccessip) throws PuiDaoFindException {
		return super.findByColumn(IVPuiUser.LAST_ACCESS_IP_FIELD, lastaccessip);
	}
}

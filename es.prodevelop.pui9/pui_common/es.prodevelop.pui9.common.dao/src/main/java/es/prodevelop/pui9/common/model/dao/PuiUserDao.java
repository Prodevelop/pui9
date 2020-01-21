package es.prodevelop.pui9.common.model.dao;

import org.springframework.stereotype.Repository;

import es.prodevelop.pui9.common.model.dao.interfaces.IPuiUserDao;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiUser;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiUserPk;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.AbstractTableDao;

/**
 * @generated
 */
@Repository
public class PuiUserDao extends AbstractTableDao<IPuiUserPk, IPuiUser> implements IPuiUserDao {
	/**
	 * @generated
	 */
	@Override
	public java.util.List<IPuiUser> findByUsr(String usr) throws PuiDaoFindException {
		return super.findByColumn(IPuiUserPk.USR_FIELD, usr);
	}

	/**
	 * @generated
	 */
	@Override
	public java.util.List<IPuiUser> findByName(String name) throws PuiDaoFindException {
		return super.findByColumn(IPuiUser.NAME_FIELD, name);
	}

	/**
	 * @generated
	 */
	@Override
	public java.util.List<IPuiUser> findByPassword(String password) throws PuiDaoFindException {
		return super.findByColumn(IPuiUser.PASSWORD_FIELD, password);
	}

	/**
	 * @generated
	 */
	@Override
	public java.util.List<IPuiUser> findByLanguage(String language) throws PuiDaoFindException {
		return super.findByColumn(IPuiUser.LANGUAGE_FIELD, language);
	}

	/**
	 * @generated
	 */
	@Override
	public java.util.List<IPuiUser> findByEmail(String email) throws PuiDaoFindException {
		return super.findByColumn(IPuiUser.EMAIL_FIELD, email);
	}

	/**
	 * @generated
	 */
	@Override
	public java.util.List<IPuiUser> findByDisabled(Integer disabled) throws PuiDaoFindException {
		return super.findByColumn(IPuiUser.DISABLED_FIELD, disabled);
	}

	/**
	 * @generated
	 */
	@Override
	public java.util.List<IPuiUser> findByDisableddate(java.time.Instant disableddate) throws PuiDaoFindException {
		return super.findByColumn(IPuiUser.DISABLED_DATE_FIELD, disableddate);
	}

	/**
	 * @generated
	 */
	@Override
	public java.util.List<IPuiUser> findByDateformat(String dateformat) throws PuiDaoFindException {
		return super.findByColumn(IPuiUser.DATEFORMAT_FIELD, dateformat);
	}

	/**
	 * @generated
	 */
	@Override
	public java.util.List<IPuiUser> findByResetpasswordtoken(String resetpasswordtoken) throws PuiDaoFindException {
		return super.findByColumn(IPuiUser.RESET_PASSWORD_TOKEN_FIELD, resetpasswordtoken);
	}

	/**
	 * @generated
	 */
	@Override
	public java.util.List<IPuiUser> findByLastaccesstime(java.time.Instant lastaccesstime) throws PuiDaoFindException {
		return super.findByColumn(IPuiUser.LAST_ACCESS_TIME_FIELD, lastaccesstime);
	}

	/**
	 * @generated
	 */
	@Override
	public java.util.List<IPuiUser> findByLastaccessip(String lastaccessip) throws PuiDaoFindException {
		return super.findByColumn(IPuiUser.LAST_ACCESS_IP_FIELD, lastaccessip);
	}
}

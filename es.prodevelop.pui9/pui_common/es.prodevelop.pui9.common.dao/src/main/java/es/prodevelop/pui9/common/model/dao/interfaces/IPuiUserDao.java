package es.prodevelop.pui9.common.model.dao.interfaces;

import es.prodevelop.pui9.common.model.dto.interfaces.IPuiUser;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiUserPk;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.interfaces.ITableDao;

/**
 * @generated
 */
public interface IPuiUserDao extends ITableDao<IPuiUserPk, IPuiUser> {
	/**
	 * @generated
	 */
	java.util.List<IPuiUser> findByUsr(String usr) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IPuiUser> findByName(String name) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IPuiUser> findByPassword(String password) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IPuiUser> findByLanguage(String language) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IPuiUser> findByEmail(String email) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IPuiUser> findByDisabled(Integer disabled) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IPuiUser> findByDisableddate(java.time.Instant disableddate) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IPuiUser> findByDateformat(String dateformat) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IPuiUser> findByResetpasswordtoken(String resetpasswordtoken) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IPuiUser> findByLastaccesstime(java.time.Instant lastaccesstime) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IPuiUser> findByLastaccessip(String lastaccessip) throws PuiDaoFindException;
}

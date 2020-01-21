package es.prodevelop.pui9.common.model.views.dao.interfaces;

import es.prodevelop.pui9.common.model.views.dto.interfaces.IVPuiUser;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.interfaces.IViewDao;

/**
 * @generated
 */
public interface IVPuiUserDao extends IViewDao<IVPuiUser> {
	/**
	 * @generated
	 */
	java.util.List<IVPuiUser> findByUsr(String usr) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IVPuiUser> findByName(String name) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IVPuiUser> findByEmail(String email) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IVPuiUser> findByLanguage(String language) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IVPuiUser> findByDateformat(String dateformat) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IVPuiUser> findByDisabled(Integer disabled) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IVPuiUser> findByDisableddate(java.time.Instant disableddate) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IVPuiUser> findByLastaccesstime(java.time.Instant lastaccesstime) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IVPuiUser> findByLastaccessip(String lastaccessip) throws PuiDaoFindException;
}

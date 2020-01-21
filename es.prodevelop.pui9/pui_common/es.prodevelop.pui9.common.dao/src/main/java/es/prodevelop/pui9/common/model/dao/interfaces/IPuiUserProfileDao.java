package es.prodevelop.pui9.common.model.dao.interfaces;

import es.prodevelop.pui9.common.model.dto.interfaces.IPuiUserProfile;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiUserProfilePk;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.interfaces.ITableDao;

/**
 * @generated
 */
public interface IPuiUserProfileDao extends ITableDao<IPuiUserProfilePk, IPuiUserProfile> {
	/**
	 * @generated
	 */
	java.util.List<IPuiUserProfile> findByUsr(String usr) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IPuiUserProfile> findByProfile(String profile) throws PuiDaoFindException;
}

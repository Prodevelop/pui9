package es.prodevelop.pui9.notifications.model.dao.interfaces;

import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.interfaces.ITableDao;
import es.prodevelop.pui9.notifications.model.dto.interfaces.IPuiUserFcm;
import es.prodevelop.pui9.notifications.model.dto.interfaces.IPuiUserFcmPk;

/**
 * @generated
 */
public interface IPuiUserFcmDao extends ITableDao<IPuiUserFcmPk, IPuiUserFcm> {
	/**
	 * @generated
	 */
	java.util.List<IPuiUserFcm> findByUsr(String usr) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IPuiUserFcm> findByToken(String token) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IPuiUserFcm> findByLastuse(java.time.Instant lastuse) throws PuiDaoFindException;
}

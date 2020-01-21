package es.prodevelop.pui9.common.model.views.dao.interfaces;

import es.prodevelop.pui9.common.model.views.dto.interfaces.IVPuiLogin;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.interfaces.IViewDao;

/**
 * @generated
 */
public interface IVPuiLoginDao extends IViewDao<IVPuiLogin> {
	/**
	 * @generated
	 */
	java.util.List<IVPuiLogin> findById(Integer id) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IVPuiLogin> findByUsr(String usr) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IVPuiLogin> findByDatetime(java.time.Instant datetime) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IVPuiLogin> findByIp(String ip) throws PuiDaoFindException;
}

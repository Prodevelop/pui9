package es.prodevelop.pui9.common.model.views.dao.interfaces;

import es.prodevelop.pui9.common.model.views.dto.interfaces.IVPuiProfile;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.interfaces.IViewDao;

/**
 * @generated
 */
public interface IVPuiProfileDao extends IViewDao<IVPuiProfile> {
	/**
	 * @generated
	 */
	java.util.List<IVPuiProfile> findByProfile(String profile) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IVPuiProfile> findByName(String name) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IVPuiProfile> findByLang(String lang) throws PuiDaoFindException;
}

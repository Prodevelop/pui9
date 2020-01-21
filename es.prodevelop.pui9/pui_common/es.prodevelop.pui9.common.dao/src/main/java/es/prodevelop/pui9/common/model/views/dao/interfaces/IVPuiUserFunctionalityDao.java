package es.prodevelop.pui9.common.model.views.dao.interfaces;

import es.prodevelop.pui9.common.model.views.dto.interfaces.IVPuiUserFunctionality;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.interfaces.IViewDao;

/**
 * @generated
 */
public interface IVPuiUserFunctionalityDao extends IViewDao<IVPuiUserFunctionality> {
	/**
	 * @generated
	 */
	java.util.List<IVPuiUserFunctionality> findByUsr(String usr) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IVPuiUserFunctionality> findByFunctionality(String functionality) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IVPuiUserFunctionality> findByFunctionalityname(String functionalityname) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IVPuiUserFunctionality> findByProfile(String profile) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IVPuiUserFunctionality> findByProfilename(String profilename) throws PuiDaoFindException;
}

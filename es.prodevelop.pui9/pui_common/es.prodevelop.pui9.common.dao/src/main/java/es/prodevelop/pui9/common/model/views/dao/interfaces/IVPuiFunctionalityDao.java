package es.prodevelop.pui9.common.model.views.dao.interfaces;

import es.prodevelop.pui9.common.model.views.dto.interfaces.IVPuiFunctionality;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.interfaces.IViewDao;

/**
 * @generated
 */
public interface IVPuiFunctionalityDao extends IViewDao<IVPuiFunctionality> {
	/**
	 * @generated
	 */
	java.util.List<IVPuiFunctionality> findByFunctionality(String functionality) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IVPuiFunctionality> findByName(String name) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IVPuiFunctionality> findBySubsystem(String subsystem) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IVPuiFunctionality> findBySubsystemname(String subsystemname) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IVPuiFunctionality> findByLang(String lang) throws PuiDaoFindException;
}

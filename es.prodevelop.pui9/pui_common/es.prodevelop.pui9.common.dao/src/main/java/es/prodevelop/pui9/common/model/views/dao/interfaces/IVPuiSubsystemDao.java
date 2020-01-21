package es.prodevelop.pui9.common.model.views.dao.interfaces;

import es.prodevelop.pui9.common.model.views.dto.interfaces.IVPuiSubsystem;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.interfaces.IViewDao;

/**
 * @generated
 */
public interface IVPuiSubsystemDao extends IViewDao<IVPuiSubsystem> {
	/**
	 * @generated
	 */
	java.util.List<IVPuiSubsystem> findBySubsystem(String subsystem) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IVPuiSubsystem> findByName(String name) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IVPuiSubsystem> findByLang(String lang) throws PuiDaoFindException;
}

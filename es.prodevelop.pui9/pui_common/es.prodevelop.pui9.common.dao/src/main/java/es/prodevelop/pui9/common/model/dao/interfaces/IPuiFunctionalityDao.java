package es.prodevelop.pui9.common.model.dao.interfaces;

import es.prodevelop.pui9.common.model.dto.interfaces.IPuiFunctionality;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiFunctionalityPk;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.interfaces.ITableDao;

/**
 * @generated
 */
public interface IPuiFunctionalityDao extends ITableDao<IPuiFunctionalityPk, IPuiFunctionality> {
	/**
	 * @generated
	 */
	java.util.List<IPuiFunctionality> findByFunctionality(String functionality) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IPuiFunctionality> findBySubsystem(String subsystem) throws PuiDaoFindException;
}

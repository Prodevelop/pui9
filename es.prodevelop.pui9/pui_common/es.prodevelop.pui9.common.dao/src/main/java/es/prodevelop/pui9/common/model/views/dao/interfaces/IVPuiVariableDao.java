package es.prodevelop.pui9.common.model.views.dao.interfaces;

import es.prodevelop.pui9.common.model.views.dto.interfaces.IVPuiVariable;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.interfaces.IViewDao;

/**
 * @generated
 */
public interface IVPuiVariableDao extends IViewDao<IVPuiVariable> {
	/**
	 * @generated
	 */
	java.util.List<IVPuiVariable> findByVariable(String variable) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IVPuiVariable> findByValue(String value) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IVPuiVariable> findByDescription(String description) throws PuiDaoFindException;
}

package es.prodevelop.pui9.common.model.dao.interfaces;

import es.prodevelop.pui9.common.model.dto.interfaces.IPuiVariable;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiVariablePk;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.interfaces.ITableDao;

/**
 * @generated
 */
public interface IPuiVariableDao extends ITableDao<IPuiVariablePk, IPuiVariable> {
	/**
	 * @generated
	 */
	java.util.List<IPuiVariable> findByVariable(String variable) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IPuiVariable> findByValue(String value) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IPuiVariable> findByDescription(String description) throws PuiDaoFindException;
}

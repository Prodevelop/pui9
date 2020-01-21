package es.prodevelop.pui9.common.model.dao;

import org.springframework.stereotype.Repository;

import es.prodevelop.pui9.common.model.dao.interfaces.IPuiVariableDao;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiVariable;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiVariablePk;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.AbstractTableDao;

/**
 * @generated
 */
@Repository
public class PuiVariableDao extends AbstractTableDao<IPuiVariablePk, IPuiVariable> implements IPuiVariableDao {
	/**
	 * @generated
	 */
	@Override
	public java.util.List<IPuiVariable> findByVariable(String variable) throws PuiDaoFindException {
		return super.findByColumn(IPuiVariablePk.VARIABLE_FIELD, variable);
	}

	/**
	 * @generated
	 */
	@Override
	public java.util.List<IPuiVariable> findByValue(String value) throws PuiDaoFindException {
		return super.findByColumn(IPuiVariable.VALUE_FIELD, value);
	}

	/**
	 * @generated
	 */
	@Override
	public java.util.List<IPuiVariable> findByDescription(String description) throws PuiDaoFindException {
		return super.findByColumn(IPuiVariable.DESCRIPTION_FIELD, description);
	}
}

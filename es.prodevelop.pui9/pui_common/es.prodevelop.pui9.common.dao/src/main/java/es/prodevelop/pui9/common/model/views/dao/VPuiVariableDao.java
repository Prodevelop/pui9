package es.prodevelop.pui9.common.model.views.dao;

import org.springframework.stereotype.Repository;

import es.prodevelop.pui9.common.model.views.dao.interfaces.IVPuiVariableDao;
import es.prodevelop.pui9.common.model.views.dto.interfaces.IVPuiVariable;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.AbstractViewDao;

/**
 * @generated
 */
@Repository
public class VPuiVariableDao extends AbstractViewDao<IVPuiVariable> implements IVPuiVariableDao {
	/**
	 * @generated
	 */
	@Override
	public java.util.List<IVPuiVariable> findByVariable(String variable) throws PuiDaoFindException {
		return super.findByColumn(IVPuiVariable.VARIABLE_FIELD, variable);
	}

	/**
	 * @generated
	 */
	@Override
	public java.util.List<IVPuiVariable> findByValue(String value) throws PuiDaoFindException {
		return super.findByColumn(IVPuiVariable.VALUE_FIELD, value);
	}

	/**
	 * @generated
	 */
	@Override
	public java.util.List<IVPuiVariable> findByDescription(String description) throws PuiDaoFindException {
		return super.findByColumn(IVPuiVariable.DESCRIPTION_FIELD, description);
	}
}

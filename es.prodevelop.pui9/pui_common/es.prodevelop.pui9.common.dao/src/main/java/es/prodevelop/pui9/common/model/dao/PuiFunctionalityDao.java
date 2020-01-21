package es.prodevelop.pui9.common.model.dao;

import org.springframework.stereotype.Repository;

import es.prodevelop.pui9.common.model.dao.interfaces.IPuiFunctionalityDao;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiFunctionality;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiFunctionalityPk;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.AbstractTableDao;

/**
 * @generated
 */
@Repository
public class PuiFunctionalityDao extends AbstractTableDao<IPuiFunctionalityPk, IPuiFunctionality>
		implements IPuiFunctionalityDao {
	/**
	 * @generated
	 */
	@Override
	public java.util.List<IPuiFunctionality> findByFunctionality(String functionality) throws PuiDaoFindException {
		return super.findByColumn(IPuiFunctionalityPk.FUNCTIONALITY_FIELD, functionality);
	}

	/**
	 * @generated
	 */
	@Override
	public java.util.List<IPuiFunctionality> findBySubsystem(String subsystem) throws PuiDaoFindException {
		return super.findByColumn(IPuiFunctionality.SUBSYSTEM_FIELD, subsystem);
	}
}

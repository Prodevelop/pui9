package es.prodevelop.pui9.common.model.dao;

import org.springframework.stereotype.Repository;

import es.prodevelop.pui9.common.model.dao.interfaces.IPuiProfileFunctionalityDao;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiProfileFunctionality;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiProfileFunctionalityPk;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.AbstractTableDao;

/**
 * @generated
 */
@Repository
public class PuiProfileFunctionalityDao extends AbstractTableDao<IPuiProfileFunctionalityPk, IPuiProfileFunctionality>
		implements IPuiProfileFunctionalityDao {
	/**
	 * @generated
	 */
	@Override
	public java.util.List<IPuiProfileFunctionality> findByProfile(String profile) throws PuiDaoFindException {
		return super.findByColumn(IPuiProfileFunctionalityPk.PROFILE_FIELD, profile);
	}

	/**
	 * @generated
	 */
	@Override
	public java.util.List<IPuiProfileFunctionality> findByFunctionality(String functionality)
			throws PuiDaoFindException {
		return super.findByColumn(IPuiProfileFunctionalityPk.FUNCTIONALITY_FIELD, functionality);
	}
}

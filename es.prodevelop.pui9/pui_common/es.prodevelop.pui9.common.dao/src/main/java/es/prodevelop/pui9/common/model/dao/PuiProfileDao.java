package es.prodevelop.pui9.common.model.dao;

import org.springframework.stereotype.Repository;

import es.prodevelop.pui9.common.model.dao.interfaces.IPuiProfileDao;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiProfile;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiProfilePk;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.AbstractTableDao;

/**
 * @generated
 */
@Repository
public class PuiProfileDao extends AbstractTableDao<IPuiProfilePk, IPuiProfile> implements IPuiProfileDao {
	/**
	 * @generated
	 */
	@Override
	public java.util.List<IPuiProfile> findByProfile(String profile) throws PuiDaoFindException {
		return super.findByColumn(IPuiProfilePk.PROFILE_FIELD, profile);
	}
}

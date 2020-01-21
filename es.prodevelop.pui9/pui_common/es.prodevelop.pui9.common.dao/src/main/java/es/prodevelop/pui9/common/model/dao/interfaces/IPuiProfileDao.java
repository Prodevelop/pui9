package es.prodevelop.pui9.common.model.dao.interfaces;

import es.prodevelop.pui9.common.model.dto.interfaces.IPuiProfile;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiProfilePk;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.interfaces.ITableDao;

/**
 * @generated
 */
public interface IPuiProfileDao extends ITableDao<IPuiProfilePk, IPuiProfile> {
	/**
	 * @generated
	 */
	java.util.List<IPuiProfile> findByProfile(String profile) throws PuiDaoFindException;
}

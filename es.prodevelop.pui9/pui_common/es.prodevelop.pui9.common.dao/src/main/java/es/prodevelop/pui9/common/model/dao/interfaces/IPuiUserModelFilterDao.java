package es.prodevelop.pui9.common.model.dao.interfaces;

import es.prodevelop.pui9.common.model.dto.interfaces.IPuiUserModelFilter;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiUserModelFilterPk;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.interfaces.ITableDao;

/**
 * @generated
 */
public interface IPuiUserModelFilterDao extends ITableDao<IPuiUserModelFilterPk, IPuiUserModelFilter> {
	/**
	 * @generated
	 */
	java.util.List<IPuiUserModelFilter> findById(Integer id) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IPuiUserModelFilter> findByUsr(String usr) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IPuiUserModelFilter> findByModel(String model) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IPuiUserModelFilter> findByLabel(String label) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IPuiUserModelFilter> findByFilter(String filter) throws PuiDaoFindException;
}

package es.prodevelop.pui9.common.model.dao.interfaces;

import es.prodevelop.pui9.common.model.dto.interfaces.IPuiModelFilter;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiModelFilterPk;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.interfaces.ITableDao;

/**
 * @generated
 */
public interface IPuiModelFilterDao extends ITableDao<IPuiModelFilterPk, IPuiModelFilter> {
	/**
	 * @generated
	 */
	java.util.List<IPuiModelFilter> findById(Integer id) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IPuiModelFilter> findByModel(String model) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IPuiModelFilter> findByLabel(String label) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IPuiModelFilter> findByDescription(String description) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IPuiModelFilter> findByFilter(String filter) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IPuiModelFilter> findByIsdefault(Integer isdefault) throws PuiDaoFindException;
}

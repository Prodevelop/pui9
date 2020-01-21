package es.prodevelop.pui9.common.model.dao.interfaces;

import es.prodevelop.pui9.common.model.dto.interfaces.IPuiModel;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiModelPk;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.interfaces.ITableDao;

/**
 * @generated
 */
public interface IPuiModelDao extends ITableDao<IPuiModelPk, IPuiModel> {
	/**
	 * @generated
	 */
	java.util.List<IPuiModel> findByModel(String model) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IPuiModel> findByEntity(String entity) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IPuiModel> findByConfiguration(String configuration) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IPuiModel> findByFilter(String filter) throws PuiDaoFindException;
}

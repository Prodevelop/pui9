package es.prodevelop.pui9.common.model.dao;

import org.springframework.stereotype.Repository;

import es.prodevelop.pui9.common.model.dao.interfaces.IPuiModelDao;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiModel;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiModelPk;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.AbstractTableDao;

/**
 * @generated
 */
@Repository
public class PuiModelDao extends AbstractTableDao<IPuiModelPk, IPuiModel> implements IPuiModelDao {
	/**
	 * @generated
	 */
	@Override
	public java.util.List<IPuiModel> findByModel(String model) throws PuiDaoFindException {
		return super.findByColumn(IPuiModelPk.MODEL_FIELD, model);
	}

	/**
	 * @generated
	 */
	@Override
	public java.util.List<IPuiModel> findByEntity(String entity) throws PuiDaoFindException {
		return super.findByColumn(IPuiModel.ENTITY_FIELD, entity);
	}

	/**
	 * @generated
	 */
	@Override
	public java.util.List<IPuiModel> findByConfiguration(String configuration) throws PuiDaoFindException {
		return super.findByColumn(IPuiModel.CONFIGURATION_FIELD, configuration);
	}

	/**
	 * @generated
	 */
	@Override
	public java.util.List<IPuiModel> findByFilter(String filter) throws PuiDaoFindException {
		return super.findByColumn(IPuiModel.FILTER_FIELD, filter);
	}
}

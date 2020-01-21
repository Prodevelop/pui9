package es.prodevelop.pui9.common.model.dao;

import org.springframework.stereotype.Repository;

import es.prodevelop.pui9.common.model.dao.interfaces.IPuiModelFilterDao;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiModelFilter;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiModelFilterPk;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.AbstractTableDao;

/**
 * @generated
 */
@Repository
public class PuiModelFilterDao extends AbstractTableDao<IPuiModelFilterPk, IPuiModelFilter>
		implements IPuiModelFilterDao {
	/**
	 * @generated
	 */
	@Override
	public java.util.List<IPuiModelFilter> findById(Integer id) throws PuiDaoFindException {
		return super.findByColumn(IPuiModelFilterPk.ID_FIELD, id);
	}

	/**
	 * @generated
	 */
	@Override
	public java.util.List<IPuiModelFilter> findByModel(String model) throws PuiDaoFindException {
		return super.findByColumn(IPuiModelFilter.MODEL_FIELD, model);
	}

	/**
	 * @generated
	 */
	@Override
	public java.util.List<IPuiModelFilter> findByLabel(String label) throws PuiDaoFindException {
		return super.findByColumn(IPuiModelFilter.LABEL_FIELD, label);
	}

	/**
	 * @generated
	 */
	@Override
	public java.util.List<IPuiModelFilter> findByDescription(String description) throws PuiDaoFindException {
		return super.findByColumn(IPuiModelFilter.DESCRIPTION_FIELD, description);
	}

	/**
	 * @generated
	 */
	@Override
	public java.util.List<IPuiModelFilter> findByFilter(String filter) throws PuiDaoFindException {
		return super.findByColumn(IPuiModelFilter.FILTER_FIELD, filter);
	}

	/**
	 * @generated
	 */
	@Override
	public java.util.List<IPuiModelFilter> findByIsdefault(Integer isdefault) throws PuiDaoFindException {
		return super.findByColumn(IPuiModelFilter.ISDEFAULT_FIELD, isdefault);
	}
}

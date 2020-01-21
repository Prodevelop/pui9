package es.prodevelop.pui9.docgen.model.views.dao;

import org.springframework.stereotype.Repository;

import es.prodevelop.pui9.docgen.model.views.dao.interfaces.IVPuiDocgenModelDao;
import es.prodevelop.pui9.docgen.model.views.dto.interfaces.IVPuiDocgenModel;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.AbstractViewDao;

/**
 * @generated
 */
@Repository
public class VPuiDocgenModelDao extends AbstractViewDao<IVPuiDocgenModel> implements IVPuiDocgenModelDao {
	/**
	 * @generated
	 */
	@Override
	public java.util.List<IVPuiDocgenModel> findByModel(String model) throws PuiDaoFindException {
		return super.findByColumn(IVPuiDocgenModel.MODEL_FIELD, model);
	}

	/**
	 * @generated
	 */
	@Override
	public java.util.List<IVPuiDocgenModel> findByEntity(String entity) throws PuiDaoFindException {
		return super.findByColumn(IVPuiDocgenModel.ENTITY_FIELD, entity);
	}

	/**
	 * @generated
	 */
	@Override
	public java.util.List<IVPuiDocgenModel> findByLabel(String label) throws PuiDaoFindException {
		return super.findByColumn(IVPuiDocgenModel.LABEL_FIELD, label);
	}
}

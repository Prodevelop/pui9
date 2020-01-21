package es.prodevelop.pui9.docgen.model.dao;

import org.springframework.stereotype.Repository;

import es.prodevelop.pui9.docgen.model.dao.interfaces.IPuiDocgenModelDao;
import es.prodevelop.pui9.docgen.model.dto.interfaces.IPuiDocgenModel;
import es.prodevelop.pui9.docgen.model.dto.interfaces.IPuiDocgenModelPk;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.AbstractTableDao;

/**
 * @generated
 */
@Repository
public class PuiDocgenModelDao extends AbstractTableDao<IPuiDocgenModelPk, IPuiDocgenModel>
		implements IPuiDocgenModelDao {
	/**
	 * @generated
	 */
	@Override
	public java.util.List<IPuiDocgenModel> findByModel(String model) throws PuiDaoFindException {
		return super.findByColumn(IPuiDocgenModelPk.MODEL_FIELD, model);
	}

	/**
	 * @generated
	 */
	@Override
	public java.util.List<IPuiDocgenModel> findByLabel(String label) throws PuiDaoFindException {
		return super.findByColumn(IPuiDocgenModel.LABEL_FIELD, label);
	}
}

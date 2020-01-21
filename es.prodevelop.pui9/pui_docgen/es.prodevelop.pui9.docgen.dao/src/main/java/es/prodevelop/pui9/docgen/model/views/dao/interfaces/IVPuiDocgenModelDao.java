package es.prodevelop.pui9.docgen.model.views.dao.interfaces;

import es.prodevelop.pui9.docgen.model.views.dto.interfaces.IVPuiDocgenModel;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.interfaces.IViewDao;

/**
 * @generated
 */
public interface IVPuiDocgenModelDao extends IViewDao<IVPuiDocgenModel> {
	/**
	 * @generated
	 */
	java.util.List<IVPuiDocgenModel> findByModel(String model) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IVPuiDocgenModel> findByEntity(String entity) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IVPuiDocgenModel> findByLabel(String label) throws PuiDaoFindException;
}

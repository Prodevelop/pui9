package es.prodevelop.pui9.docgen.model.views.dao.interfaces;

import es.prodevelop.pui9.docgen.model.views.dto.interfaces.IVPuiDocgenTemplate;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.interfaces.IViewDao;

/**
 * @generated
 */
public interface IVPuiDocgenTemplateDao extends IViewDao<IVPuiDocgenTemplate> {
	/**
	 * @generated
	 */
	java.util.List<IVPuiDocgenTemplate> findById(Integer id) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IVPuiDocgenTemplate> findByName(String name) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IVPuiDocgenTemplate> findByDescription(String description) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IVPuiDocgenTemplate> findByMainmodel(String mainmodel) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IVPuiDocgenTemplate> findByModels(String models) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IVPuiDocgenTemplate> findByFilename(String filename) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IVPuiDocgenTemplate> findByColumnfilename(String columnfilename) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IVPuiDocgenTemplate> findByLabel(String label) throws PuiDaoFindException;
}

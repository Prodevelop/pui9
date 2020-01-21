package es.prodevelop.pui9.docgen.model.dao.interfaces;

import es.prodevelop.pui9.docgen.model.dto.interfaces.IPuiDocgenTemplate;
import es.prodevelop.pui9.docgen.model.dto.interfaces.IPuiDocgenTemplatePk;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.interfaces.ITableDao;

/**
 * @generated
 */
public interface IPuiDocgenTemplateDao extends ITableDao<IPuiDocgenTemplatePk, IPuiDocgenTemplate> {
	/**
	 * @generated
	 */
	java.util.List<IPuiDocgenTemplate> findById(Integer id) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IPuiDocgenTemplate> findByName(String name) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IPuiDocgenTemplate> findByDescription(String description) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IPuiDocgenTemplate> findByMainmodel(String mainmodel) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IPuiDocgenTemplate> findByModels(String models) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IPuiDocgenTemplate> findByFilename(String filename) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IPuiDocgenTemplate> findByMapping(String mapping) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IPuiDocgenTemplate> findByFilter(String filter) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IPuiDocgenTemplate> findByParameters(String parameters) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IPuiDocgenTemplate> findByColumnfilename(String columnfilename) throws PuiDaoFindException;
}

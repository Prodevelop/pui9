package es.prodevelop.pui9.documents.model.views.dao.interfaces;

import es.prodevelop.pui9.documents.model.views.dto.interfaces.IVPuiDocument;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.interfaces.IViewDao;

/**
 * @generated
 */
public interface IVPuiDocumentDao extends IViewDao<IVPuiDocument> {
	/**
	 * @generated
	 */
	java.util.List<IVPuiDocument> findById(Integer id) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IVPuiDocument> findByModel(String model) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IVPuiDocument> findByPk(String pk) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IVPuiDocument> findByDescription(String description) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IVPuiDocument> findByLanguage(String language) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IVPuiDocument> findByFilename(String filename) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IVPuiDocument> findByFilenameorig(String filenameorig) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IVPuiDocument> findByRole(String role) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IVPuiDocument> findByRoledescription(String roledescription) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IVPuiDocument> findByThumbnails(String thumbnails) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IVPuiDocument> findByUrl(String url) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IVPuiDocument> findByDatetime(java.time.Instant datetime) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IVPuiDocument> findByLang(String lang) throws PuiDaoFindException;
}

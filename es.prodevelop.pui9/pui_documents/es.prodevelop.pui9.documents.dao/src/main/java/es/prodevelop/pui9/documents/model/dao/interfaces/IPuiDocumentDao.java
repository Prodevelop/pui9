package es.prodevelop.pui9.documents.model.dao.interfaces;

import es.prodevelop.pui9.documents.model.dto.interfaces.IPuiDocument;
import es.prodevelop.pui9.documents.model.dto.interfaces.IPuiDocumentPk;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.interfaces.ITableDao;

/**
 * @generated
 */
public interface IPuiDocumentDao extends ITableDao<IPuiDocumentPk, IPuiDocument> {
	/**
	 * @generated
	 */
	java.util.List<IPuiDocument> findById(Integer id) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IPuiDocument> findByModel(String model) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IPuiDocument> findByPk(String pk) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IPuiDocument> findByLanguage(String language) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IPuiDocument> findByDescription(String description) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IPuiDocument> findByFilename(String filename) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IPuiDocument> findByFilenameorig(String filenameorig) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IPuiDocument> findByRole(String role) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IPuiDocument> findByThumbnails(String thumbnails) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IPuiDocument> findByDatetime(java.time.Instant datetime) throws PuiDaoFindException;
}

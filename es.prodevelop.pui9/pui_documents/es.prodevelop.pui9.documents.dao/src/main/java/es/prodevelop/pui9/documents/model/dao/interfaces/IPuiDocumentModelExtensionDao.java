package es.prodevelop.pui9.documents.model.dao.interfaces;

import es.prodevelop.pui9.documents.model.dto.interfaces.IPuiDocumentModelExtension;
import es.prodevelop.pui9.documents.model.dto.interfaces.IPuiDocumentModelExtensionPk;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.interfaces.ITableDao;

/**
 * @generated
 */
public interface IPuiDocumentModelExtensionDao
		extends ITableDao<IPuiDocumentModelExtensionPk, IPuiDocumentModelExtension> {
	/**
	 * @generated
	 */
	java.util.List<IPuiDocumentModelExtension> findByModel(String model) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IPuiDocumentModelExtension> findByExtension(String extension) throws PuiDaoFindException;
}

package es.prodevelop.pui9.documents.model.dao;

import org.springframework.stereotype.Repository;

import es.prodevelop.pui9.documents.model.dao.interfaces.IPuiDocumentExtensionDao;
import es.prodevelop.pui9.documents.model.dto.interfaces.IPuiDocumentExtension;
import es.prodevelop.pui9.documents.model.dto.interfaces.IPuiDocumentExtensionPk;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.AbstractTableDao;

/**
 * @generated
 */
@Repository
public class PuiDocumentExtensionDao extends AbstractTableDao<IPuiDocumentExtensionPk, IPuiDocumentExtension>
		implements IPuiDocumentExtensionDao {
	/**
	 * @generated
	 */
	@Override
	public java.util.List<IPuiDocumentExtension> findByExtension(String extension) throws PuiDaoFindException {
		return super.findByColumn(IPuiDocumentExtensionPk.EXTENSION_FIELD, extension);
	}

	/**
	 * @generated
	 */
	@Override
	public java.util.List<IPuiDocumentExtension> findByMaxsize(Integer maxsize) throws PuiDaoFindException {
		return super.findByColumn(IPuiDocumentExtension.MAX_SIZE_FIELD, maxsize);
	}
}

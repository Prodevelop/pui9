package es.prodevelop.pui9.documents.model.dao.interfaces;

import es.prodevelop.pui9.documents.model.dto.interfaces.IPuiDocumentRole;
import es.prodevelop.pui9.documents.model.dto.interfaces.IPuiDocumentRolePk;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.interfaces.ITableDao;

/**
 * @generated
 */
public interface IPuiDocumentRoleDao extends ITableDao<IPuiDocumentRolePk, IPuiDocumentRole> {
	/**
	 * @generated
	 */
	java.util.List<IPuiDocumentRole> findByRole(String role) throws PuiDaoFindException;
}

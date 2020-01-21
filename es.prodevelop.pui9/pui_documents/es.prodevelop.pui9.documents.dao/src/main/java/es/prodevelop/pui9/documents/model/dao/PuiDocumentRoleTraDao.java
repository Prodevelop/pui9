package es.prodevelop.pui9.documents.model.dao;

import org.springframework.stereotype.Repository;

import es.prodevelop.pui9.documents.model.dao.interfaces.IPuiDocumentRoleTraDao;
import es.prodevelop.pui9.documents.model.dto.interfaces.IPuiDocumentRoleTra;
import es.prodevelop.pui9.documents.model.dto.interfaces.IPuiDocumentRoleTraPk;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.AbstractTableDao;

/**
 * @generated
 */
@Repository
public class PuiDocumentRoleTraDao extends AbstractTableDao<IPuiDocumentRoleTraPk, IPuiDocumentRoleTra>
		implements IPuiDocumentRoleTraDao {
	/**
	 * @generated
	 */
	@Override
	public java.util.List<IPuiDocumentRoleTra> findByRole(String role) throws PuiDaoFindException {
		return super.findByColumn(IPuiDocumentRoleTraPk.ROLE_FIELD, role);
	}

	/**
	 * @generated
	 */
	@Override
	public java.util.List<IPuiDocumentRoleTra> findByLang(String lang) throws PuiDaoFindException {
		return super.findByColumn(IPuiDocumentRoleTraPk.LANG_FIELD, lang);
	}

	/**
	 * @generated
	 */
	@Override
	public java.util.List<IPuiDocumentRoleTra> findByLangstatus(Integer langstatus) throws PuiDaoFindException {
		return super.findByColumn(IPuiDocumentRoleTra.LANG_STATUS_FIELD, langstatus);
	}

	/**
	 * @generated
	 */
	@Override
	public java.util.List<IPuiDocumentRoleTra> findByDescription(String description) throws PuiDaoFindException {
		return super.findByColumn(IPuiDocumentRoleTra.DESCRIPTION_FIELD, description);
	}
}

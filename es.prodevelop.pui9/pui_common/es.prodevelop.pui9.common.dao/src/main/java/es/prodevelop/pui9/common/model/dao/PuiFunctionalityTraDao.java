package es.prodevelop.pui9.common.model.dao;

import org.springframework.stereotype.Repository;

import es.prodevelop.pui9.common.model.dao.interfaces.IPuiFunctionalityTraDao;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiFunctionalityTra;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiFunctionalityTraPk;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.AbstractTableDao;

/**
 * @generated
 */
@Repository
public class PuiFunctionalityTraDao extends AbstractTableDao<IPuiFunctionalityTraPk, IPuiFunctionalityTra>
		implements IPuiFunctionalityTraDao {
	/**
	 * @generated
	 */
	@Override
	public java.util.List<IPuiFunctionalityTra> findByFunctionality(String functionality) throws PuiDaoFindException {
		return super.findByColumn(IPuiFunctionalityTraPk.FUNCTIONALITY_FIELD, functionality);
	}

	/**
	 * @generated
	 */
	@Override
	public java.util.List<IPuiFunctionalityTra> findByLang(String lang) throws PuiDaoFindException {
		return super.findByColumn(IPuiFunctionalityTraPk.LANG_FIELD, lang);
	}

	/**
	 * @generated
	 */
	@Override
	public java.util.List<IPuiFunctionalityTra> findByLangstatus(Integer langstatus) throws PuiDaoFindException {
		return super.findByColumn(IPuiFunctionalityTra.LANG_STATUS_FIELD, langstatus);
	}

	/**
	 * @generated
	 */
	@Override
	public java.util.List<IPuiFunctionalityTra> findByName(String name) throws PuiDaoFindException {
		return super.findByColumn(IPuiFunctionalityTra.NAME_FIELD, name);
	}
}

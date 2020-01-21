package es.prodevelop.pui9.common.model.dao;

import org.springframework.stereotype.Repository;

import es.prodevelop.pui9.common.model.dao.interfaces.IPuiProfileTraDao;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiProfileTra;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiProfileTraPk;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.AbstractTableDao;

/**
 * @generated
 */
@Repository
public class PuiProfileTraDao extends AbstractTableDao<IPuiProfileTraPk, IPuiProfileTra> implements IPuiProfileTraDao {
	/**
	 * @generated
	 */
	@Override
	public java.util.List<IPuiProfileTra> findByProfile(String profile) throws PuiDaoFindException {
		return super.findByColumn(IPuiProfileTraPk.PROFILE_FIELD, profile);
	}

	/**
	 * @generated
	 */
	@Override
	public java.util.List<IPuiProfileTra> findByLang(String lang) throws PuiDaoFindException {
		return super.findByColumn(IPuiProfileTraPk.LANG_FIELD, lang);
	}

	/**
	 * @generated
	 */
	@Override
	public java.util.List<IPuiProfileTra> findByLangstatus(Integer langstatus) throws PuiDaoFindException {
		return super.findByColumn(IPuiProfileTra.LANG_STATUS_FIELD, langstatus);
	}

	/**
	 * @generated
	 */
	@Override
	public java.util.List<IPuiProfileTra> findByName(String name) throws PuiDaoFindException {
		return super.findByColumn(IPuiProfileTra.NAME_FIELD, name);
	}
}

package es.prodevelop.pui9.common.model.dao;

import org.springframework.stereotype.Repository;

import es.prodevelop.pui9.common.model.dao.interfaces.IPuiLanguageDao;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiLanguage;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiLanguagePk;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.AbstractTableDao;

/**
 * @generated
 */
@Repository
public class PuiLanguageDao extends AbstractTableDao<IPuiLanguagePk, IPuiLanguage> implements IPuiLanguageDao {
	/**
	 * @generated
	 */
	@Override
	public java.util.List<IPuiLanguage> findByIsocode(String isocode) throws PuiDaoFindException {
		return super.findByColumn(IPuiLanguagePk.ISOCODE_FIELD, isocode);
	}

	/**
	 * @generated
	 */
	@Override
	public java.util.List<IPuiLanguage> findByName(String name) throws PuiDaoFindException {
		return super.findByColumn(IPuiLanguage.NAME_FIELD, name);
	}

	/**
	 * @generated
	 */
	@Override
	public java.util.List<IPuiLanguage> findByIsdefault(Integer isdefault) throws PuiDaoFindException {
		return super.findByColumn(IPuiLanguage.ISDEFAULT_FIELD, isdefault);
	}
}

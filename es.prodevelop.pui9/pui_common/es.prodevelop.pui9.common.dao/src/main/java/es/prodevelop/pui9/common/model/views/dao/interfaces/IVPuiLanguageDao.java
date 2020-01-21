package es.prodevelop.pui9.common.model.views.dao.interfaces;

import es.prodevelop.pui9.common.model.views.dto.interfaces.IVPuiLanguage;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.interfaces.IViewDao;

/**
 * @generated
 */
public interface IVPuiLanguageDao extends IViewDao<IVPuiLanguage> {
	/**
	 * @generated
	 */
	java.util.List<IVPuiLanguage> findByIsocode(String isocode) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IVPuiLanguage> findByName(String name) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IVPuiLanguage> findByIsdefault(Integer isdefault) throws PuiDaoFindException;
}

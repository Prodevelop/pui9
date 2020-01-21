package es.prodevelop.pui9.common.model.dao.interfaces;

import es.prodevelop.pui9.common.model.dto.interfaces.IPuiProfileTra;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiProfileTraPk;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.interfaces.ITableDao;

/**
 * @generated
 */
public interface IPuiProfileTraDao extends ITableDao<IPuiProfileTraPk, IPuiProfileTra> {
	/**
	 * @generated
	 */
	java.util.List<IPuiProfileTra> findByProfile(String profile) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IPuiProfileTra> findByLang(String lang) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IPuiProfileTra> findByLangstatus(Integer langstatus) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IPuiProfileTra> findByName(String name) throws PuiDaoFindException;
}

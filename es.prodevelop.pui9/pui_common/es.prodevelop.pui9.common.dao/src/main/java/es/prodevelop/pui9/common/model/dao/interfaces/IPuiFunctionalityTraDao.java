package es.prodevelop.pui9.common.model.dao.interfaces;

import es.prodevelop.pui9.common.model.dto.interfaces.IPuiFunctionalityTra;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiFunctionalityTraPk;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.interfaces.ITableDao;

/**
 * @generated
 */
public interface IPuiFunctionalityTraDao extends ITableDao<IPuiFunctionalityTraPk, IPuiFunctionalityTra> {
	/**
	 * @generated
	 */
	java.util.List<IPuiFunctionalityTra> findByFunctionality(String functionality) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IPuiFunctionalityTra> findByLang(String lang) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IPuiFunctionalityTra> findByLangstatus(Integer langstatus) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IPuiFunctionalityTra> findByName(String name) throws PuiDaoFindException;
}

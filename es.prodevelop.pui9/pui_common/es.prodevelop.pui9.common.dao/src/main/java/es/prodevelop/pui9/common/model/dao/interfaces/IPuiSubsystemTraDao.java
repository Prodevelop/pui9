package es.prodevelop.pui9.common.model.dao.interfaces;

import es.prodevelop.pui9.common.model.dto.interfaces.IPuiSubsystemTra;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiSubsystemTraPk;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.interfaces.ITableDao;

/**
 * @generated
 */
public interface IPuiSubsystemTraDao extends ITableDao<IPuiSubsystemTraPk, IPuiSubsystemTra> {
	/**
	 * @generated
	 */
	java.util.List<IPuiSubsystemTra> findBySubsystem(String subsystem) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IPuiSubsystemTra> findByLang(String lang) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IPuiSubsystemTra> findByLangstatus(Integer langstatus) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IPuiSubsystemTra> findByName(String name) throws PuiDaoFindException;
}

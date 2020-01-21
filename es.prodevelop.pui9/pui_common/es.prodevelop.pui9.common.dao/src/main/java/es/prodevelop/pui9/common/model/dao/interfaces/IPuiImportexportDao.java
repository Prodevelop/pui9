package es.prodevelop.pui9.common.model.dao.interfaces;

import es.prodevelop.pui9.common.model.dto.interfaces.IPuiImportexport;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiImportexportPk;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.interfaces.ITableDao;

/**
 * @generated
 */
public interface IPuiImportexportDao extends ITableDao<IPuiImportexportPk, IPuiImportexport> {
	/**
	 * @generated
	 */
	java.util.List<IPuiImportexport> findById(Integer id) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IPuiImportexport> findByModel(String model) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IPuiImportexport> findByUsr(String usr) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IPuiImportexport> findByDatetime(java.time.Instant datetime) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IPuiImportexport> findByFilenamecsv(String filenamecsv) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IPuiImportexport> findByFilenamejson(String filenamejson) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IPuiImportexport> findByExecuted(Integer executed) throws PuiDaoFindException;
}

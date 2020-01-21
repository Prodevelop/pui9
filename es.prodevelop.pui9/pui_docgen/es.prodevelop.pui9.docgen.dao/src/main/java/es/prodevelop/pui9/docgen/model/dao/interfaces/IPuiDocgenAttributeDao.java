package es.prodevelop.pui9.docgen.model.dao.interfaces;

import es.prodevelop.pui9.docgen.model.dto.interfaces.IPuiDocgenAttribute;
import es.prodevelop.pui9.docgen.model.dto.interfaces.IPuiDocgenAttributePk;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.interfaces.ITableDao;

/**
 * @generated
 */
public interface IPuiDocgenAttributeDao extends ITableDao<IPuiDocgenAttributePk, IPuiDocgenAttribute> {
	/**
	 * @generated
	 */
	java.util.List<IPuiDocgenAttribute> findById(String id) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IPuiDocgenAttribute> findByLabel(String label) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IPuiDocgenAttribute> findByValue(String value) throws PuiDaoFindException;
}

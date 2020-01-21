package es.prodevelop.pui9.common.model.dao.interfaces;

import es.prodevelop.pui9.common.model.dto.interfaces.IPuiMenu;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiMenuPk;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.interfaces.ITableDao;

/**
 * @generated
 */
public interface IPuiMenuDao extends ITableDao<IPuiMenuPk, IPuiMenu> {
	/**
	 * @generated
	 */
	java.util.List<IPuiMenu> findByNode(Integer node) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IPuiMenu> findByParent(Integer parent) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IPuiMenu> findByModel(String model) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IPuiMenu> findByComponent(String component) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IPuiMenu> findByFunctionality(String functionality) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IPuiMenu> findByLabel(String label) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IPuiMenu> findByIconlabel(String iconlabel) throws PuiDaoFindException;
}

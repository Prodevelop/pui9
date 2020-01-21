package es.prodevelop.pui9.common.model.dao;

import org.springframework.stereotype.Repository;

import es.prodevelop.pui9.common.model.dao.interfaces.IPuiMenuDao;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiMenu;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiMenuPk;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.AbstractTableDao;

/**
 * @generated
 */
@Repository
public class PuiMenuDao extends AbstractTableDao<IPuiMenuPk, IPuiMenu> implements IPuiMenuDao {
	/**
	 * @generated
	 */
	@Override
	public java.util.List<IPuiMenu> findByNode(Integer node) throws PuiDaoFindException {
		return super.findByColumn(IPuiMenuPk.NODE_FIELD, node);
	}

	/**
	 * @generated
	 */
	@Override
	public java.util.List<IPuiMenu> findByParent(Integer parent) throws PuiDaoFindException {
		return super.findByColumn(IPuiMenu.PARENT_FIELD, parent);
	}

	/**
	 * @generated
	 */
	@Override
	public java.util.List<IPuiMenu> findByModel(String model) throws PuiDaoFindException {
		return super.findByColumn(IPuiMenu.MODEL_FIELD, model);
	}

	/**
	 * @generated
	 */
	@Override
	public java.util.List<IPuiMenu> findByComponent(String component) throws PuiDaoFindException {
		return super.findByColumn(IPuiMenu.COMPONENT_FIELD, component);
	}

	/**
	 * @generated
	 */
	@Override
	public java.util.List<IPuiMenu> findByFunctionality(String functionality) throws PuiDaoFindException {
		return super.findByColumn(IPuiMenu.FUNCTIONALITY_FIELD, functionality);
	}

	/**
	 * @generated
	 */
	@Override
	public java.util.List<IPuiMenu> findByLabel(String label) throws PuiDaoFindException {
		return super.findByColumn(IPuiMenu.LABEL_FIELD, label);
	}

	/**
	 * @generated
	 */
	@Override
	public java.util.List<IPuiMenu> findByIconlabel(String iconlabel) throws PuiDaoFindException {
		return super.findByColumn(IPuiMenu.ICON_LABEL_FIELD, iconlabel);
	}
}

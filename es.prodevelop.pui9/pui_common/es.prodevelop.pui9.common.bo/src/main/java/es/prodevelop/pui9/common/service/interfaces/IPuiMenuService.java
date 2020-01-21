package es.prodevelop.pui9.common.service.interfaces;

import java.util.List;

import es.prodevelop.pui9.common.model.dao.interfaces.IPuiMenuDao;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiMenu;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiMenuPk;
import es.prodevelop.pui9.model.dao.interfaces.INullViewDao;
import es.prodevelop.pui9.model.dto.interfaces.INullView;
import es.prodevelop.pui9.service.interfaces.IService;

/**
 * @generated
 */
public interface IPuiMenuService extends IService<IPuiMenuPk, IPuiMenu, INullView, IPuiMenuDao, INullViewDao> {

	List<IPuiMenu> getMenuForLoggerUser();

}
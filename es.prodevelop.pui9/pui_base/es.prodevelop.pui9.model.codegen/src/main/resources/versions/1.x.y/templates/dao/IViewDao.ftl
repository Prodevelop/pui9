<#compress>
package ${config.server.daoJavaPackage}.model.views.dao.interfaces;

import ${config.server.dtoJavaPackage}.model.views.dto.interfaces.I${config.selectedView.javaName};

import es.prodevelop.pui9.model.dao.interfaces.IViewDao;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;

/**
 * @generated
 */
public interface I${config.selectedView.javaName}Dao extends IViewDao<I${config.selectedView.javaName}> {
<#list config.selectedView.columns as column>
	/** 
	 * @generated
	 */
	java.util.List<I${config.selectedView.javaName}> findBy${column.javaName?cap_first}(${column.javaTypeString} ${column.javaName}) throws PuiDaoFindException;
</#list>
}
</#compress>

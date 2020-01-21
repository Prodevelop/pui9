<#compress>
package ${config.server.daoJavaPackage}.model.dao.interfaces;

import ${config.server.dtoJavaPackage}.model.dto.interfaces.I${config.selectedTable.javaName};
import ${config.server.dtoJavaPackage}.model.dto.interfaces.I${config.selectedTable.javaName}Pk;

<#assign superclass = "">
<#if config.selectedTable.withGeometry>
import es.prodevelop.pui9.geo.dao.interfaces.IGeoDao;
	<#assign superclass = "IGeoDao">
<#else>
import es.prodevelop.pui9.model.dao.interfaces.ITableDao;
	<#assign superclass = "ITableDao">
</#if>
import es.prodevelop.pui9.exceptions.PuiDaoFindException;

/**
 * @generated
 */
public interface I${config.selectedTable.javaName}Dao extends ${superclass}<I${config.selectedTable.javaName}Pk, I${config.selectedTable.javaName}> {
<#list config.selectedTable.columns as column>
	/** 
	 * @generated
	 */
	java.util.List<I${config.selectedTable.javaName}> findBy${column.javaName?cap_first}(${column.javaTypeString} ${column.javaName}) throws PuiDaoFindException;
</#list>
}
</#compress>

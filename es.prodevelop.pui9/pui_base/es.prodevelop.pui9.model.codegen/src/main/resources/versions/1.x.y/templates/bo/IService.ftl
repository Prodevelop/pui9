<#compress>
package ${config.server.boJavaPackage}.service.interfaces;

import es.prodevelop.pui9.service.interfaces.IService;

<#assign className = "">
<#assign tableDaoClass = "">
<#assign tableDtoClass = "">
<#assign tableDtoPkClass = "">
<#assign viewDaoClass = "">
<#assign viewDtoClass = "">
<#if config.selectedTable??>
	<#assign className = config.selectedTable.javaName>
	<#assign tableDaoClass = "I" + config.selectedTable.javaName + "Dao">
import ${config.server.daoJavaPackage}.model.dao.interfaces.I${config.selectedTable.javaName}Dao;
	<#assign tableDtoClass = "I" + config.selectedTable.javaName>
import ${config.server.dtoJavaPackage}.model.dto.interfaces.I${config.selectedTable.javaName};
	<#assign tableDtoPkClass = "I" + config.selectedTable.javaName + "Pk">
import ${config.server.dtoJavaPackage}.model.dto.interfaces.I${config.selectedTable.javaName}Pk;
<#else>
	<#assign tableDaoClass = "INullTableDao">
import es.prodevelop.pui9.model.dao.interfaces.INullTableDao;
	<#assign tableDtoClass = "INullTable">
import es.prodevelop.pui9.model.dto.interfaces.INullTable;
	<#assign tableDtoPkClass = "INullTablePk">
import es.prodevelop.pui9.model.dto.interfaces.INullTablePk;
</#if>

<#if config.selectedView??>
	<#if !className?has_content>
		<#assign className = config.selectedView.javaName>
	</#if>
	<#assign viewDaoClass = "I" + config.selectedView.javaName + "Dao">
import ${config.server.daoJavaPackage}.model.views.dao.interfaces.I${config.selectedView.javaName}Dao;
	<#assign viewDtoClass = "I" + config.selectedView.javaName>
import ${config.server.dtoJavaPackage}.model.views.dto.interfaces.I${config.selectedView.javaName};
<#else>
	<#assign viewDaoClass = "INullViewDao">
import es.prodevelop.pui9.model.dao.interfaces.INullViewDao;
	<#assign viewDtoClass = "INullView">
import es.prodevelop.pui9.model.dto.interfaces.INullView;
</#if>

/**
 * @generated
 */
public interface I${className}Service extends IService<${tableDtoPkClass}, ${tableDtoClass}, ${viewDtoClass}, ${tableDaoClass}, ${viewDaoClass}> {

}
</#compress>

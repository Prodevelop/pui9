<#compress>
package ${config.server.daoJavaPackage}.model.dao;

<#assign superclass = "">
<#if config.selectedTable.withGeometry>
import es.prodevelop.pui9.geo.dao.AbstractGeoDao;
	<#assign superclass = "AbstractGeoDao">
<#else>
import es.prodevelop.pui9.model.dao.AbstractTableDao;
	<#assign superclass = "AbstractTableDao">
</#if>

import ${config.server.daoJavaPackage}.model.dao.interfaces.I${config.selectedTable.javaName}Dao;
import ${config.server.dtoJavaPackage}.model.dto.interfaces.I${config.selectedTable.javaName};
import ${config.server.dtoJavaPackage}.model.dto.interfaces.I${config.selectedTable.javaName}Pk;

import es.prodevelop.pui9.exceptions.PuiDaoFindException;

import org.springframework.stereotype.Repository;

/**
 * @generated
 */
@Repository
public class ${config.selectedTable.javaName}Dao extends ${superclass}<I${config.selectedTable.javaName}Pk, I${config.selectedTable.javaName}> implements I${config.selectedTable.javaName}Dao {
<#list config.selectedTable.columns as column>
	/** 
	 * @generated
	 */
	@Override
	public java.util.List<I${config.selectedTable.javaName}> findBy${column.javaName?cap_first}(${column.javaTypeString} ${column.javaName}) throws PuiDaoFindException {
		return super.findByColumn(I${config.selectedTable.javaName}<#if column.pk>Pk</#if>.${column.dbName?upper_case}_FIELD, ${column.javaName});
	}
</#list>
}
</#compress>

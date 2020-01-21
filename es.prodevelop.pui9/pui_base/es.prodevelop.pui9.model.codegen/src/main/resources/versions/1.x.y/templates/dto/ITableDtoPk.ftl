<#compress>
package ${config.server.dtoJavaPackage}.model.dto.interfaces;

<#assign superclass = "">
<#if config.selectedTable.withGeometry>
import es.prodevelop.pui9.geo.dto.interfaces.IGeoDto;
	<#assign superclass = "IGeoDto">
<#else>
import es.prodevelop.pui9.model.dto.interfaces.ITableDto;
	<#assign superclass = "ITableDto">
</#if>

/**
 * @generated
 */
public interface I${config.selectedTable.javaName}Pk extends ${superclass} {

<#list config.selectedTable.primaryKeys as pk>
	/**
	 * @generated
	 */
	String ${pk.dbName?upper_case}_COLUMN = "${pk.dbName?lower_case}";
	/**
	 * @generated
	 */
	String ${pk.dbName?upper_case}_FIELD = "${pk.javaName}";

</#list>

<#list config.selectedTable.primaryKeys as pk>
	/**
	 * @generated
	 */
	${pk.javaTypeString} get${pk.javaName?cap_first}();
	/**
	 * @generated
	 */
	void set${pk.javaName?cap_first}(${pk.javaTypeString} ${pk.javaName});

</#list>
}
</#compress>

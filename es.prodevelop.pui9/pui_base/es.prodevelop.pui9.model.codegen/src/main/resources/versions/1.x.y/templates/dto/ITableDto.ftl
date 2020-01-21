<#compress>
package ${config.server.dtoJavaPackage}.model.dto.interfaces;

/**
 * @generated
 */
<#assign hasTranslation = config.selectedTable.translationTable??>
public interface I${config.selectedTable.javaName} extends I${config.selectedTable.javaName}Pk <#if hasTranslation>, I${config.selectedTable.translationTable.javaName}</#if> {

<#list config.selectedTable.columns as column>
	<#if column.pk>
		<#continue>
	</#if>
	/**
	 * @generated
	 */
	String ${column.dbName?upper_case}_COLUMN = "${column.dbName?lower_case}";
	/**
	 * @generated
	 */
	String ${column.dbName?upper_case}_FIELD = "${column.javaName}";
</#list>

<#list config.selectedTable.columns as column>
	<#if column.pk>
		<#continue>
	</#if>
	/**
	 * @generated
	 */
	${column.javaTypeString} get${column.javaName?cap_first}();
	/**
	 * @generated
	 */
	void set${column.javaName?cap_first}(${column.javaTypeString} ${column.javaName});
</#list>
}
</#compress>

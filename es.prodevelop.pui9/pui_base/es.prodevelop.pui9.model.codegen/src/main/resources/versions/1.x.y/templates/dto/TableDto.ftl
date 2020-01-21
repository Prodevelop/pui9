<#compress>
package ${config.server.dtoJavaPackage}.model.dto;

<#assign dtoHasColumns = (config.selectedTable.columns?size > config.selectedTable.primaryKeys?size)>
import es.prodevelop.pui9.annotations.PuiEntity;
import es.prodevelop.pui9.annotations.PuiField;
import es.prodevelop.pui9.enums.ColumnType;
import ${config.server.dtoJavaPackage}.model.dto.interfaces.I${config.selectedTable.javaName};
<#if config.selectedTable.translationTable??>
import ${config.server.dtoJavaPackage}.model.dto.interfaces.I${config.selectedTable.translationTable.javaName};
</#if>

/**
 * @generated
 */
@PuiEntity(tablename = "${config.selectedTable.dbName?lower_case}"<#if (config.selectedTable.translationTable??)>, tabletranslationname = "${config.selectedTable.translationTable.dbName?lower_case}"</#if>)
public class ${config.selectedTable.javaName} extends ${config.selectedTable.javaName}Pk implements I${config.selectedTable.javaName} {

	/**
	 * @generated
	 */
	private static final long serialVersionUID = 1L;

<#list config.selectedTable.columns as column>
	<#if column.pk>
		<#continue>
	</#if>
	/**
	 * @generated
	 */
	@PuiField(columnname = I${config.selectedTable.javaName}.${column.dbName?upper_case}_COLUMN, ispk = false, nullable = ${column.nullable?c}, type = ColumnType.${column.columnType}, autoincrementable = ${column.autoincrementable?c}, maxlength = ${column.javaSize?c}, islang = false, isgeometry = ${column.geometry?c}, issequence = ${column.sequence?c})
	private ${column.javaTypeString} ${column.javaName}<#if column.javaDefaultValue?has_content> = ${column.javaDefaultValue}</#if>;
</#list>

<#if config.selectedTable.translationTable??>
	<#list config.selectedTable.translationTable.columns as langColumn>
		<#if langColumn.pk && langColumn.dbName != "lang">
			<#continue>
		</#if>
	/**
	 * @generated
	 */
	@PuiField(columnname = I${config.selectedTable.javaName}.${langColumn.dbName?upper_case}_COLUMN, ispk = false, nullable = ${langColumn.nullable?c}, type = ColumnType.${langColumn.columnType}, autoincrementable = ${langColumn.autoincrementable?c}, maxlength = ${langColumn.javaSize?c}, islang = true, isgeometry = ${langColumn.geometry?c}, issequence = ${langColumn.sequence?c})
	private ${langColumn.javaTypeString} ${langColumn.javaName}<#if langColumn.javaDefaultValue?has_content> = ${langColumn.javaDefaultValue}</#if>;
	</#list>
</#if>

<#if dtoHasColumns>
	<#list config.selectedTable.columns as column>
		<#if column.pk>
			<#continue>
		</#if>
	/**
	 * @generated
	 */
	@Override
	public ${column.javaTypeString} get${column.javaName?cap_first}() {
		return ${column.javaName};
	}

	/**
	 * @generated
	 */
	@Override
	public void set${column.javaName?cap_first}(${column.javaTypeString} ${column.javaName}) {
		<#if column.javaType == "DATE">
		this.${column.javaName} = ${column.javaName} != null ? (${column.javaTypeString}) ${column.javaName}.clone() : null;
		<#else>
		this.${column.javaName} = ${column.javaName};
		</#if>
	}
	</#list>
</#if>

<#if config.selectedTable.translationTable??>
	<#list config.selectedTable.translationTable.columns as langColumn>
		<#if langColumn.pk && langColumn.dbName != "lang">
			<#continue>
		</#if>
	/**
	 * @generated
	 */
	@Override
	public ${langColumn.javaTypeString} get${langColumn.javaName?cap_first}() {
		return ${langColumn.javaName};
	}

	/**
	 * @generated
	 */
	@Override
	public void set${langColumn.javaName?cap_first}(${langColumn.javaTypeString} ${langColumn.javaName}) {
		<#if langColumn.javaType == "DATE">
		this.${langColumn.javaName} = ${langColumn.javaName} != null ? (${langColumn.javaTypeString}) ${langColumn.javaName}.clone() : null;
		<#else>
		this.${langColumn.javaName} = ${langColumn.javaName};
		</#if>
	}
	</#list>
</#if>

}
</#compress>

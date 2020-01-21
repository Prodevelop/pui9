<#compress>
package ${config.server.dtoJavaPackage}.model.dto;

<#assign dtoHasColumns = (config.selectedTable.columns?size > config.selectedTable.primaryKeys?size)>
import es.prodevelop.pui9.enums.ColumnType;
import es.prodevelop.pui9.annotations.PuiField;
import es.prodevelop.pui9.utils.PuiObjectUtils;

<#assign superclass = "">
<#if config.selectedTable.withGeometry>
import es.prodevelop.pui9.geo.dto.AbstractGeoDto;
import ${config.server.dtoJavaPackage}.model.dto.interfaces.I${config.selectedTable.javaName};
	<#assign superclass = "AbstractGeoDto">
<#else>
import es.prodevelop.pui9.model.dto.AbstractTableDto;
	<#assign superclass = "AbstractTableDto">
</#if>
import ${config.server.dtoJavaPackage}.model.dto.interfaces.I${config.selectedTable.javaName}Pk;

/**
 * @generated
 */
public class ${config.selectedTable.javaName}Pk extends ${superclass} implements I${config.selectedTable.javaName}Pk {

	/**
	 * @generated
	 */
	private static final long serialVersionUID = 1L;

<#list config.selectedTable.primaryKeys as pk>
	/**
	 * @generated
	 */
	@PuiField(columnname = I${config.selectedTable.javaName}Pk.${pk.dbName?upper_case}_COLUMN, ispk = true, nullable = false, type = ColumnType.${pk.columnType}, autoincrementable = ${pk.autoincrementable?c}, maxlength = ${pk.javaSize?c}, islang = false, isgeometry = ${pk.geometry?c}, issequence = ${pk.sequence?c})
	private ${pk.javaTypeString} ${pk.javaName}<#if pk.javaDefaultValue?has_content> = ${pk.javaDefaultValue}</#if>;
</#list>

	/**
	 * @generated
	 */
	public ${config.selectedTable.javaName}Pk() {
	}

<#assign params = "">
<#list config.selectedTable.primaryKeys as pk>
	<#assign params += pk.javaTypeString + " "+ pk.javaName>
	<#if pk?has_next>
		<#assign params += ", ">
	</#if>
</#list>

	/**
	 * @generated
	 */
	public ${config.selectedTable.javaName}Pk(${params}) {
<#list config.selectedTable.primaryKeys as pk>
		this.${pk.javaName} = ${pk.javaName};
</#list>
	}

<#list config.selectedTable.primaryKeys as pk>
	/**
	 * @generated
	 */
	@Override
	public ${pk.javaTypeString} get${pk.javaName?cap_first}() {
		return ${pk.javaName};
	}

	/**
	 * @generated
	 */
	@Override
	public void set${pk.javaName?cap_first}(${pk.javaTypeString} ${pk.javaName}) {
		this.${pk.javaName} = ${pk.javaName};
	}
</#list>

	/**
	 * @generated
	 */
	@Override
	@SuppressWarnings("unchecked")
	public ${config.selectedTable.javaName}Pk createPk() {
		${config.selectedTable.javaName}Pk pk = new ${config.selectedTable.javaName}Pk();
		PuiObjectUtils.copyProperties(pk, this);
		return pk;
	}
<#if config.selectedTable.withGeometry>
	
	<#list config.selectedTable.columns as column>
		<#if !column.geometry>
			<#continue>
		</#if>
	/**
	 * @generated
	 */
	@Override
	public String getGeometryFieldName() {
		return I${config.selectedTable.javaName}.${column.dbName?upper_case}_FIELD;
	}
	</#list>
</#if>
}
</#compress>

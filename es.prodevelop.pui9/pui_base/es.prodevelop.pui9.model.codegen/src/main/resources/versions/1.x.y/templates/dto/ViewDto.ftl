<#compress>
package ${config.server.dtoJavaPackage}.model.views.dto;

import ${config.server.dtoJavaPackage}.model.views.dto.interfaces.I${config.selectedView.javaName};

import es.prodevelop.pui9.annotations.PuiEntity;
import es.prodevelop.pui9.annotations.PuiField;
import es.prodevelop.pui9.annotations.PuiViewColumn;
import es.prodevelop.pui9.enums.ColumnType;
import es.prodevelop.pui9.enums.ColumnVisibility;

import es.prodevelop.pui9.model.dto.AbstractViewDto;

/**
 * @generated
 */
@PuiEntity(tablename = "${config.selectedView.dbName?lower_case}")
public class ${config.selectedView.javaName} extends AbstractViewDto implements I${config.selectedView.javaName} {

	/**
	 * @generated
	 */
	private static final long serialVersionUID = 1L;

<#list config.selectedView.columns as column>
	/**
	 * @generated
	 */
	@PuiField(columnname = I${config.selectedView.javaName}.${column.dbName?upper_case}_COLUMN, ispk = false, nullable = ${column.nullable?c}, type = ColumnType.${column.columnType}, autoincrementable = ${column.autoincrementable?c}, maxlength = ${column.javaSize?c}, islang = false, isgeometry = ${column.geometry?c}, issequence = ${column.sequence?c})
	@PuiViewColumn(order = ${column?index + 1}, visibility = ColumnVisibility.${column.columnVisibility})
	private ${column.javaTypeString} ${column.javaName};
</#list>

<#list config.selectedView.columns as column>
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

}
</#compress>

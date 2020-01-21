<#compress>
package ${config.server.dtoJavaPackage}.model.views.dto.interfaces;

import es.prodevelop.pui9.model.dto.interfaces.IViewDto;

/**
 * @generated
 */
public interface I${config.selectedView.javaName} extends IViewDto {

<#list config.selectedView.columns as column>
	/**
	 * @generated
	 */
	String ${column.dbName?upper_case}_COLUMN = "${column.dbName?lower_case}";
	/**
	 * @generated
	 */
	String ${column.dbName?upper_case}_FIELD = "${column.javaName}";
</#list>

<#list config.selectedView.columns as column>
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

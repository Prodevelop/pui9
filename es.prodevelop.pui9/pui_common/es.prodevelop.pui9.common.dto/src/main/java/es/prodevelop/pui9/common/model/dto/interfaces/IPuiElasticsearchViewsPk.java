package es.prodevelop.pui9.common.model.dto.interfaces;

import es.prodevelop.pui9.model.dto.interfaces.ITableDto;

public interface IPuiElasticsearchViewsPk extends ITableDto {

	/**
	 * @generated
	 */
	String APPNAME_COLUMN = "appname";

	/**
	 * @generated
	 */
	String APPNAME_FIELD = "appname";

	/**
	 * @generated
	 */
	String VIEWNAME_COLUMN = "viewname";

	/**
	 * @generated
	 */
	String VIEWNAME_FIELD = "viewname";

	/**
	 * @generated
	 */
	String getAppname();

	/**
	 * @generated
	 */
	void setAppname(String appname);

	/**
	 * @generated
	 */
	String getViewname();

	/**
	 * @generated
	 */
	void setViewname(String viewname);
}

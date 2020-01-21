package es.prodevelop.pui9.common.model.dto;

import es.prodevelop.pui9.annotations.PuiField;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiElasticsearchViewsPk;
import es.prodevelop.pui9.enums.ColumnType;
import es.prodevelop.pui9.model.dto.AbstractTableDto;
import es.prodevelop.pui9.utils.PuiObjectUtils;

/**
 * @generated
 */
public class PuiElasticsearchViewsPk extends AbstractTableDto implements IPuiElasticsearchViewsPk {
	/**
	 * @generated
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * @generated
	 */
	@PuiField(columnname = IPuiElasticsearchViewsPk.APPNAME_COLUMN, ispk = true, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	private String appname = "DEFAULT";
	/**
	 * @generated
	 */
	@PuiField(columnname = IPuiElasticsearchViewsPk.VIEWNAME_COLUMN, ispk = true, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	private String viewname;

	/**
	 * @generated
	 */
	public PuiElasticsearchViewsPk(String appname, String viewname) {
		this.appname = appname;
		this.viewname = viewname;
	}

	/**
	 * @generated
	 */
	public PuiElasticsearchViewsPk() {
	}

	/**
	 * @generated
	 */
	@Override
	public String getAppname() {
		return appname;
	}

	/**
	 * @generated
	 */
	@Override
	public void setAppname(String appname) {
		this.appname = appname;
	}

	/**
	 * @generated
	 */
	@Override
	public String getViewname() {
		return viewname;
	}

	/**
	 * @generated
	 */
	@Override
	public void setViewname(String viewname) {
		this.viewname = viewname;
	}

	/**
	 * @generated
	 */
	@Override
	@SuppressWarnings("unchecked")
	public PuiElasticsearchViewsPk createPk() {
		PuiElasticsearchViewsPk pk = new PuiElasticsearchViewsPk();
		PuiObjectUtils.copyProperties(pk, this);
		return pk;
	}
}

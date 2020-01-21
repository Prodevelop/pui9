package es.prodevelop.pui9.common.model.dto;

import es.prodevelop.pui9.annotations.PuiEntity;
import es.prodevelop.pui9.annotations.PuiField;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiElasticsearchViews;
import es.prodevelop.pui9.enums.ColumnType;

/**
 * @generated
 */
@PuiEntity(tablename = "pui_elasticsearch_views")
public class PuiElasticsearchViews extends PuiElasticsearchViewsPk implements IPuiElasticsearchViews {
	/**
	 * @generated
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * @generated
	 */
	@PuiField(columnname = IPuiElasticsearchViews.IDENTITY_FIELDS_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	private String identityfields = "";

	/**
	 * @generated
	 */
	@Override
	public String getIdentityfields() {
		return identityfields;
	}

	/**
	 * @generated
	 */
	@Override
	public void setIdentityfields(String identityfields) {
		this.identityfields = identityfields;
	}
}

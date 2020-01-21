package es.prodevelop.pui9.common.model.dto;

import es.prodevelop.pui9.annotations.PuiEntity;
import es.prodevelop.pui9.annotations.PuiField;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiModel;
import es.prodevelop.pui9.enums.ColumnType;

/**
 * @generated
 */
@PuiEntity(tablename = "pui_model")
public class PuiModel extends PuiModelPk implements IPuiModel {
	/**
	 * @generated
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * @generated
	 */
	@PuiField(columnname = IPuiModel.ENTITY_COLUMN, ispk = false, nullable = true, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	private String entity;
	/**
	 * @generated
	 */
	@PuiField(columnname = IPuiModel.CONFIGURATION_COLUMN, ispk = false, nullable = true, type = ColumnType.text, autoincrementable = false, maxlength = -1, islang = false, isgeometry = false, issequence = false)
	private String configuration;
	/**
	 * @generated
	 */
	@PuiField(columnname = IPuiModel.FILTER_COLUMN, ispk = false, nullable = true, type = ColumnType.text, autoincrementable = false, maxlength = -1, islang = false, isgeometry = false, issequence = false)
	private String filter;

	/**
	 * @generated
	 */
	@Override
	public String getEntity() {
		return entity;
	}

	/**
	 * @generated
	 */
	@Override
	public void setEntity(String entity) {
		this.entity = entity;
	}

	/**
	 * @generated
	 */
	@Override
	public String getConfiguration() {
		return configuration;
	}

	/**
	 * @generated
	 */
	@Override
	public void setConfiguration(String configuration) {
		this.configuration = configuration;
	}

	/**
	 * @generated
	 */
	@Override
	public String getFilter() {
		return filter;
	}

	/**
	 * @generated
	 */
	@Override
	public void setFilter(String filter) {
		this.filter = filter;
	}
}

package es.prodevelop.pui9.common.model.dto;

import es.prodevelop.pui9.annotations.PuiEntity;
import es.prodevelop.pui9.annotations.PuiField;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiModelFilter;
import es.prodevelop.pui9.enums.ColumnType;

/**
 * @generated
 */
@PuiEntity(tablename = "pui_model_filter")
public class PuiModelFilter extends PuiModelFilterPk implements IPuiModelFilter {
	/**
	 * @generated
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * @generated
	 */
	@PuiField(columnname = IPuiModelFilter.MODEL_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	private String model;
	/**
	 * @generated
	 */
	@PuiField(columnname = IPuiModelFilter.LABEL_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 200, islang = false, isgeometry = false, issequence = false)
	private String label;
	/**
	 * @generated
	 */
	@PuiField(columnname = IPuiModelFilter.DESCRIPTION_COLUMN, ispk = false, nullable = true, type = ColumnType.text, autoincrementable = false, maxlength = 300, islang = false, isgeometry = false, issequence = false)
	private String description;
	/**
	 * @generated
	 */
	@PuiField(columnname = IPuiModelFilter.FILTER_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = -1, islang = false, isgeometry = false, issequence = false)
	private String filter;
	/**
	 * @generated
	 */
	@PuiField(columnname = IPuiModelFilter.ISDEFAULT_COLUMN, ispk = false, nullable = false, type = ColumnType.numeric, autoincrementable = false, maxlength = -1, islang = false, isgeometry = false, issequence = false)
	private Integer isdefault = 0;

	/**
	 * @generated
	 */
	@Override
	public String getModel() {
		return model;
	}

	/**
	 * @generated
	 */
	@Override
	public void setModel(String model) {
		this.model = model;
	}

	/**
	 * @generated
	 */
	@Override
	public String getLabel() {
		return label;
	}

	/**
	 * @generated
	 */
	@Override
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * @generated
	 */
	@Override
	public String getDescription() {
		return description;
	}

	/**
	 * @generated
	 */
	@Override
	public void setDescription(String description) {
		this.description = description;
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

	/**
	 * @generated
	 */
	@Override
	public Integer getIsdefault() {
		return isdefault;
	}

	/**
	 * @generated
	 */
	@Override
	public void setIsdefault(Integer isdefault) {
		this.isdefault = isdefault;
	}
}

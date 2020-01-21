package es.prodevelop.pui9.common.model.dto;

import es.prodevelop.pui9.annotations.PuiEntity;
import es.prodevelop.pui9.annotations.PuiField;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiVariable;
import es.prodevelop.pui9.enums.ColumnType;

/**
 * @generated
 */
@PuiEntity(tablename = "pui_variable")
public class PuiVariable extends PuiVariablePk implements IPuiVariable {
	/**
	 * @generated
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * @generated
	 */
	@PuiField(columnname = IPuiVariable.VALUE_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = -1, islang = false, isgeometry = false, issequence = false)
	private String value;
	/**
	 * @generated
	 */
	@PuiField(columnname = IPuiVariable.DESCRIPTION_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 500, islang = false, isgeometry = false, issequence = false)
	private String description;

	/**
	 * @generated
	 */
	@Override
	public String getValue() {
		return value;
	}

	/**
	 * @generated
	 */
	@Override
	public void setValue(String value) {
		this.value = value;
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
}

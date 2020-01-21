package es.prodevelop.pui9.common.model.views.dto;

import es.prodevelop.pui9.annotations.PuiEntity;
import es.prodevelop.pui9.annotations.PuiField;
import es.prodevelop.pui9.annotations.PuiViewColumn;
import es.prodevelop.pui9.common.model.views.dto.interfaces.IVPuiVariable;
import es.prodevelop.pui9.enums.ColumnType;
import es.prodevelop.pui9.enums.ColumnVisibility;
import es.prodevelop.pui9.model.dto.AbstractViewDto;

/**
 * @generated
 */
@PuiEntity(tablename = "v_pui_variable")
public class VPuiVariable extends AbstractViewDto implements IVPuiVariable {
	/**
	 * @generated
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * @generated
	 */
	@PuiField(columnname = IVPuiVariable.VARIABLE_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 50, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 1, visibility = ColumnVisibility.visible)
	private String variable;
	/**
	 * @generated
	 */
	@PuiField(columnname = IVPuiVariable.VALUE_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = -1, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 2, visibility = ColumnVisibility.visible)
	private String value;
	/**
	 * @generated
	 */
	@PuiField(columnname = IVPuiVariable.DESCRIPTION_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 500, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 3, visibility = ColumnVisibility.visible)
	private String description;

	/**
	 * @generated
	 */
	@Override
	public String getVariable() {
		return variable;
	}

	/**
	 * @generated
	 */
	@Override
	public void setVariable(String variable) {
		this.variable = variable;
	}

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

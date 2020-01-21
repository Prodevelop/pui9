package es.prodevelop.pui9.common.model.dto;

import es.prodevelop.pui9.annotations.PuiField;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiVariablePk;
import es.prodevelop.pui9.enums.ColumnType;
import es.prodevelop.pui9.model.dto.AbstractTableDto;
import es.prodevelop.pui9.utils.PuiObjectUtils;

/**
 * @generated
 */
public class PuiVariablePk extends AbstractTableDto implements IPuiVariablePk {
	/**
	 * @generated
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * @generated
	 */
	@PuiField(columnname = IPuiVariablePk.VARIABLE_COLUMN, ispk = true, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 50, islang = false, isgeometry = false, issequence = false)
	private String variable;

	/**
	 * @generated
	 */
	public PuiVariablePk() {
	}

	/**
	 * @generated
	 */
	public PuiVariablePk(String variable) {
		this.variable = variable;
	}

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
	@SuppressWarnings("unchecked")
	public PuiVariablePk createPk() {
		PuiVariablePk pk = new PuiVariablePk();
		PuiObjectUtils.copyProperties(pk, this);
		return pk;
	}
}

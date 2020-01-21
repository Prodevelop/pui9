package es.prodevelop.pui9.common.model.dto;

import es.prodevelop.pui9.annotations.PuiField;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiSubsystemPk;
import es.prodevelop.pui9.enums.ColumnType;
import es.prodevelop.pui9.model.dto.AbstractTableDto;
import es.prodevelop.pui9.utils.PuiObjectUtils;

/**
 * @generated
 */
public class PuiSubsystemPk extends AbstractTableDto implements IPuiSubsystemPk {
	/**
	 * @generated
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * @generated
	 */
	@PuiField(columnname = IPuiSubsystemPk.SUBSYSTEM_COLUMN, ispk = true, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 3, islang = false, isgeometry = false, issequence = false)
	private String subsystem;

	/**
	 * @generated
	 */
	public PuiSubsystemPk() {
	}

	/**
	 * @generated
	 */
	public PuiSubsystemPk(String subsystem) {
		this.subsystem = subsystem;
	}

	/**
	 * @generated
	 */
	@Override
	public String getSubsystem() {
		return subsystem;
	}

	/**
	 * @generated
	 */
	@Override
	public void setSubsystem(String subsystem) {
		this.subsystem = subsystem;
	}

	/**
	 * @generated
	 */
	@Override
	@SuppressWarnings("unchecked")
	public PuiSubsystemPk createPk() {
		PuiSubsystemPk pk = new PuiSubsystemPk();
		PuiObjectUtils.copyProperties(pk, this);
		return pk;
	}
}

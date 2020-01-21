package es.prodevelop.pui9.common.model.dto;

import es.prodevelop.pui9.annotations.PuiField;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiSubsystemTraPk;
import es.prodevelop.pui9.enums.ColumnType;
import es.prodevelop.pui9.model.dto.AbstractTableDto;
import es.prodevelop.pui9.utils.PuiObjectUtils;

/**
 * @generated
 */
public class PuiSubsystemTraPk extends AbstractTableDto implements IPuiSubsystemTraPk {
	/**
	 * @generated
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * @generated
	 */
	@PuiField(columnname = IPuiSubsystemTraPk.SUBSYSTEM_COLUMN, ispk = true, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 3, islang = false, isgeometry = false, issequence = false)
	private String subsystem;
	/**
	 * @generated
	 */
	@PuiField(columnname = IPuiSubsystemTraPk.LANG_COLUMN, ispk = true, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 2, islang = false, isgeometry = false, issequence = false)
	private String lang;

	/**
	 * @generated
	 */
	public PuiSubsystemTraPk() {
	}

	/**
	 * @generated
	 */
	public PuiSubsystemTraPk(String lang, String subsystem) {
		this.lang = lang;
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
	public String getLang() {
		return lang;
	}

	/**
	 * @generated
	 */
	@Override
	public void setLang(String lang) {
		this.lang = lang;
	}

	/**
	 * @generated
	 */
	@Override
	@SuppressWarnings("unchecked")
	public PuiSubsystemTraPk createPk() {
		PuiSubsystemTraPk pk = new PuiSubsystemTraPk();
		PuiObjectUtils.copyProperties(pk, this);
		return pk;
	}
}

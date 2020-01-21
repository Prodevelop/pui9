package es.prodevelop.pui9.common.model.dto;

import es.prodevelop.pui9.annotations.PuiField;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiUserPk;
import es.prodevelop.pui9.enums.ColumnType;
import es.prodevelop.pui9.model.dto.AbstractTableDto;
import es.prodevelop.pui9.utils.PuiObjectUtils;

/**
 * @generated
 */
public class PuiUserPk extends AbstractTableDto implements IPuiUserPk {
	/**
	 * @generated
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * @generated
	 */
	@PuiField(columnname = IPuiUserPk.USR_COLUMN, ispk = true, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	private String usr;

	/**
	 * @generated
	 */
	public PuiUserPk() {
	}

	/**
	 * @generated
	 */
	public PuiUserPk(String usr) {
		this.usr = usr;
	}

	/**
	 * @generated
	 */
	@Override
	public String getUsr() {
		return usr;
	}

	/**
	 * @generated
	 */
	@Override
	public void setUsr(String usr) {
		this.usr = usr;
	}

	/**
	 * @generated
	 */
	@Override
	@SuppressWarnings("unchecked")
	public PuiUserPk createPk() {
		PuiUserPk pk = new PuiUserPk();
		PuiObjectUtils.copyProperties(pk, this);
		return pk;
	}
}

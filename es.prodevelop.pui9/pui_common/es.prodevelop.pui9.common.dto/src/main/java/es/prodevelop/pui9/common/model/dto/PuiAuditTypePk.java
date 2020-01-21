package es.prodevelop.pui9.common.model.dto;

import es.prodevelop.pui9.annotations.PuiField;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiAuditTypePk;
import es.prodevelop.pui9.enums.ColumnType;
import es.prodevelop.pui9.model.dto.AbstractTableDto;
import es.prodevelop.pui9.utils.PuiObjectUtils;

/**
 * @generated
 */
public class PuiAuditTypePk extends AbstractTableDto implements IPuiAuditTypePk {
	/**
	 * @generated
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * @generated
	 */
	@PuiField(columnname = IPuiAuditTypePk.TYPE_COLUMN, ispk = true, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 50, islang = false, isgeometry = false, issequence = false)
	private String type;

	/**
	 * @generated
	 */
	public PuiAuditTypePk() {
	}

	/**
	 * @generated
	 */
	public PuiAuditTypePk(String type) {
		this.type = type;
	}

	/**
	 * @generated
	 */
	@Override
	public String getType() {
		return type;
	}

	/**
	 * @generated
	 */
	@Override
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @generated
	 */
	@Override
	@SuppressWarnings("unchecked")
	public PuiAuditTypePk createPk() {
		PuiAuditTypePk pk = new PuiAuditTypePk();
		PuiObjectUtils.copyProperties(pk, this);
		return pk;
	}
}

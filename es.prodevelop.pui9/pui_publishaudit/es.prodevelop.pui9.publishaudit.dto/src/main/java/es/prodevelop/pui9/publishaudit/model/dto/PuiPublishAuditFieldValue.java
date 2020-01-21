package es.prodevelop.pui9.publishaudit.model.dto;

import es.prodevelop.pui9.annotations.PuiEntity;
import es.prodevelop.pui9.annotations.PuiField;
import es.prodevelop.pui9.enums.ColumnType;
import es.prodevelop.pui9.publishaudit.model.dto.interfaces.IPuiPublishAuditFieldValue;

/**
 * @generated
 */
@PuiEntity(tablename = "pui_publish_audit_field_value")
public class PuiPublishAuditFieldValue extends PuiPublishAuditFieldValuePk implements IPuiPublishAuditFieldValue {
	/**
	 * @generated
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * @generated
	 */
	@PuiField(columnname = IPuiPublishAuditFieldValue.ID_FIELD_COLUMN, ispk = false, nullable = false, type = ColumnType.numeric, autoincrementable = false, maxlength = -1, islang = false, isgeometry = false, issequence = false)
	private Integer idfield;
	/**
	 * @generated
	 */
	@PuiField(columnname = IPuiPublishAuditFieldValue.OLD_VALUE_COLUMN, ispk = false, nullable = true, type = ColumnType.text, autoincrementable = false, maxlength = 200, islang = false, isgeometry = false, issequence = false)
	private String oldvalue;
	/**
	 * @generated
	 */
	@PuiField(columnname = IPuiPublishAuditFieldValue.NEW_VALUE_COLUMN, ispk = false, nullable = true, type = ColumnType.text, autoincrementable = false, maxlength = 200, islang = false, isgeometry = false, issequence = false)
	private String newvalue;

	/**
	 * @generated
	 */
	@Override
	public Integer getIdfield() {
		return idfield;
	}

	/**
	 * @generated
	 */
	@Override
	public void setIdfield(Integer idfield) {
		this.idfield = idfield;
	}

	/**
	 * @generated
	 */
	@Override
	public String getOldvalue() {
		return oldvalue;
	}

	/**
	 * @generated
	 */
	@Override
	public void setOldvalue(String oldvalue) {
		this.oldvalue = oldvalue;
	}

	/**
	 * @generated
	 */
	@Override
	public String getNewvalue() {
		return newvalue;
	}

	/**
	 * @generated
	 */
	@Override
	public void setNewvalue(String newvalue) {
		this.newvalue = newvalue;
	}
}

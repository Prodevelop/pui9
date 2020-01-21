package es.prodevelop.pui9.publishaudit.model.dto;

import es.prodevelop.pui9.annotations.PuiEntity;
import es.prodevelop.pui9.annotations.PuiField;
import es.prodevelop.pui9.enums.ColumnType;
import es.prodevelop.pui9.publishaudit.model.dto.interfaces.IPuiPublishAuditField;

/**
 * @generated
 */
@PuiEntity(tablename = "pui_publish_audit_field")
public class PuiPublishAuditField extends PuiPublishAuditFieldPk implements IPuiPublishAuditField {
	/**
	 * @generated
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * @generated
	 */
	@PuiField(columnname = IPuiPublishAuditField.ID_TOPIC_COLUMN, ispk = false, nullable = false, type = ColumnType.numeric, autoincrementable = false, maxlength = -1, islang = false, isgeometry = false, issequence = false)
	private Integer idtopic;
	/**
	 * @generated
	 */
	@PuiField(columnname = IPuiPublishAuditField.FIELD_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	private String field;

	/**
	 * @generated
	 */
	@Override
	public Integer getIdtopic() {
		return idtopic;
	}

	/**
	 * @generated
	 */
	@Override
	public void setIdtopic(Integer idtopic) {
		this.idtopic = idtopic;
	}

	/**
	 * @generated
	 */
	@Override
	public String getField() {
		return field;
	}

	/**
	 * @generated
	 */
	@Override
	public void setField(String field) {
		this.field = field;
	}
}

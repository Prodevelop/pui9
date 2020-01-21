package es.prodevelop.pui9.publishaudit.model.dto;

import es.prodevelop.pui9.annotations.PuiEntity;
import es.prodevelop.pui9.annotations.PuiField;
import es.prodevelop.pui9.enums.ColumnType;
import es.prodevelop.pui9.publishaudit.model.dto.interfaces.IPuiPublishAuditEntity;

/**
 * @generated
 */
@PuiEntity(tablename = "pui_publish_audit_entity")
public class PuiPublishAuditEntity extends PuiPublishAuditEntityPk implements IPuiPublishAuditEntity {
	/**
	 * @generated
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * @generated
	 */
	@PuiField(columnname = IPuiPublishAuditEntity.ENTITY_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	private String entity;
	/**
	 * @generated
	 */
	@PuiField(columnname = IPuiPublishAuditEntity.AUDIT_INSERT_COLUMN, ispk = false, nullable = false, type = ColumnType.numeric, autoincrementable = false, maxlength = -1, islang = false, isgeometry = false, issequence = false)
	private Integer auditinsert = 0;
	/**
	 * @generated
	 */
	@PuiField(columnname = IPuiPublishAuditEntity.AUDIT_UPDATE_COLUMN, ispk = false, nullable = false, type = ColumnType.numeric, autoincrementable = false, maxlength = -1, islang = false, isgeometry = false, issequence = false)
	private Integer auditupdate = 0;
	/**
	 * @generated
	 */
	@PuiField(columnname = IPuiPublishAuditEntity.AUDIT_DELETE_COLUMN, ispk = false, nullable = false, type = ColumnType.numeric, autoincrementable = false, maxlength = -1, islang = false, isgeometry = false, issequence = false)
	private Integer auditdelete = 0;

	/**
	 * @generated
	 */
	@Override
	public String getEntity() {
		return entity;
	}

	/**
	 * @generated
	 */
	@Override
	public void setEntity(String entity) {
		this.entity = entity;
	}

	/**
	 * @generated
	 */
	@Override
	public Integer getAuditinsert() {
		return auditinsert;
	}

	/**
	 * @generated
	 */
	@Override
	public void setAuditinsert(Integer auditinsert) {
		this.auditinsert = auditinsert;
	}

	/**
	 * @generated
	 */
	@Override
	public Integer getAuditupdate() {
		return auditupdate;
	}

	/**
	 * @generated
	 */
	@Override
	public void setAuditupdate(Integer auditupdate) {
		this.auditupdate = auditupdate;
	}

	/**
	 * @generated
	 */
	@Override
	public Integer getAuditdelete() {
		return auditdelete;
	}

	/**
	 * @generated
	 */
	@Override
	public void setAuditdelete(Integer auditdelete) {
		this.auditdelete = auditdelete;
	}
}

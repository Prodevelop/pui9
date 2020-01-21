package es.prodevelop.pui9.common.model.dto;

import es.prodevelop.pui9.annotations.PuiEntity;
import es.prodevelop.pui9.annotations.PuiField;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiAudit;
import es.prodevelop.pui9.enums.ColumnType;

/**
 * @generated
 */
@PuiEntity(tablename = "pui_audit")
public class PuiAudit extends PuiAuditPk implements IPuiAudit {
	/**
	 * @generated
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * @generated
	 */
	@PuiField(columnname = IPuiAudit.MODEL_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	private String model;
	/**
	 * @generated
	 */
	@PuiField(columnname = IPuiAudit.TYPE_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 50, islang = false, isgeometry = false, issequence = false)
	private String type;
	/**
	 * @generated
	 */
	@PuiField(columnname = IPuiAudit.PK_COLUMN, ispk = false, nullable = true, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	private String pk;
	/**
	 * @generated
	 */
	@PuiField(columnname = IPuiAudit.DATETIME_COLUMN, ispk = false, nullable = false, type = ColumnType.datetime, autoincrementable = false, maxlength = -1, islang = false, isgeometry = false, issequence = false)
	private java.time.Instant datetime;
	/**
	 * @generated
	 */
	@PuiField(columnname = IPuiAudit.USR_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	private String usr;
	/**
	 * @generated
	 */
	@PuiField(columnname = IPuiAudit.IP_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	private String ip = "0.0.0.0";
	/**
	 * @generated
	 */
	@PuiField(columnname = IPuiAudit.DTO_COLUMN, ispk = false, nullable = true, type = ColumnType.text, autoincrementable = false, maxlength = -1, islang = false, isgeometry = false, issequence = false)
	private String dto;

	/**
	 * @generated
	 */
	@Override
	public String getModel() {
		return model;
	}

	/**
	 * @generated
	 */
	@Override
	public void setModel(String model) {
		this.model = model;
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
	public String getPk() {
		return pk;
	}

	/**
	 * @generated
	 */
	@Override
	public void setPk(String pk) {
		this.pk = pk;
	}

	/**
	 * @generated
	 */
	@Override
	public java.time.Instant getDatetime() {
		return datetime;
	}

	/**
	 * @generated
	 */
	@Override
	public void setDatetime(java.time.Instant datetime) {
		this.datetime = datetime;
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
	public String getIp() {
		return ip;
	}

	/**
	 * @generated
	 */
	@Override
	public void setIp(String ip) {
		this.ip = ip;
	}

	/**
	 * @generated
	 */
	@Override
	public String getDto() {
		return dto;
	}

	/**
	 * @generated
	 */
	@Override
	public void setDto(String dto) {
		this.dto = dto;
	}
}

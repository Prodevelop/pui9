package es.prodevelop.pui9.common.model.views.dto;

import es.prodevelop.pui9.annotations.PuiEntity;
import es.prodevelop.pui9.annotations.PuiField;
import es.prodevelop.pui9.annotations.PuiViewColumn;
import es.prodevelop.pui9.common.model.views.dto.interfaces.IVPuiAudit;
import es.prodevelop.pui9.enums.ColumnType;
import es.prodevelop.pui9.enums.ColumnVisibility;
import es.prodevelop.pui9.model.dto.AbstractViewDto;

/**
 * @generated
 */
@PuiEntity(tablename = "v_pui_audit")
public class VPuiAudit extends AbstractViewDto implements IVPuiAudit {
	/**
	 * @generated
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * @generated
	 */
	@PuiField(columnname = IVPuiAudit.ID_COLUMN, ispk = false, nullable = false, type = ColumnType.numeric, autoincrementable = false, maxlength = -1, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 1, visibility = ColumnVisibility.visible)
	private Integer id;
	/**
	 * @generated
	 */
	@PuiField(columnname = IVPuiAudit.DATETIME_COLUMN, ispk = false, nullable = false, type = ColumnType.datetime, autoincrementable = false, maxlength = -1, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 2, visibility = ColumnVisibility.visible)
	private java.time.Instant datetime;
	/**
	 * @generated
	 */
	@PuiField(columnname = IVPuiAudit.USR_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 3, visibility = ColumnVisibility.visible)
	private String usr;
	/**
	 * @generated
	 */
	@PuiField(columnname = IVPuiAudit.IP_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 4, visibility = ColumnVisibility.visible)
	private String ip;
	/**
	 * @generated
	 */
	@PuiField(columnname = IVPuiAudit.TYPE_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 50, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 5, visibility = ColumnVisibility.visible)
	private String type;
	/**
	 * @generated
	 */
	@PuiField(columnname = IVPuiAudit.MODEL_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 6, visibility = ColumnVisibility.visible)
	private String model;
	/**
	 * @generated
	 */
	@PuiField(columnname = IVPuiAudit.PK_COLUMN, ispk = false, nullable = true, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 7, visibility = ColumnVisibility.visible)
	private String pk;
	/**
	 * @generated
	 */
	@PuiField(columnname = IVPuiAudit.DTO_COLUMN, ispk = false, nullable = true, type = ColumnType.text, autoincrementable = false, maxlength = -1, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 8, visibility = ColumnVisibility.visible)
	private String dto;

	/**
	 * @generated
	 */
	@Override
	public Integer getId() {
		return id;
	}

	/**
	 * @generated
	 */
	@Override
	public void setId(Integer id) {
		this.id = id;
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

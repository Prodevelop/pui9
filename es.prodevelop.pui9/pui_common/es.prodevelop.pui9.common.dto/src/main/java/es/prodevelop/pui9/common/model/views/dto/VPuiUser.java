package es.prodevelop.pui9.common.model.views.dto;

import es.prodevelop.pui9.annotations.PuiEntity;
import es.prodevelop.pui9.annotations.PuiField;
import es.prodevelop.pui9.annotations.PuiViewColumn;
import es.prodevelop.pui9.common.model.views.dto.interfaces.IVPuiUser;
import es.prodevelop.pui9.enums.ColumnType;
import es.prodevelop.pui9.enums.ColumnVisibility;
import es.prodevelop.pui9.model.dto.AbstractViewDto;

/**
 * @generated
 */
@PuiEntity(tablename = "v_pui_user")
public class VPuiUser extends AbstractViewDto implements IVPuiUser {
	/**
	 * @generated
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * @generated
	 */
	@PuiField(columnname = IVPuiUser.USR_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 1, visibility = ColumnVisibility.visible)
	private String usr;
	/**
	 * @generated
	 */
	@PuiField(columnname = IVPuiUser.NAME_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 200, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 2, visibility = ColumnVisibility.visible)
	private String name;
	/**
	 * @generated
	 */
	@PuiField(columnname = IVPuiUser.EMAIL_COLUMN, ispk = false, nullable = true, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 3, visibility = ColumnVisibility.visible)
	private String email;
	/**
	 * @generated
	 */
	@PuiField(columnname = IVPuiUser.LANGUAGE_COLUMN, ispk = false, nullable = true, type = ColumnType.text, autoincrementable = false, maxlength = 2, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 4, visibility = ColumnVisibility.visible)
	private String language;
	/**
	 * @generated
	 */
	@PuiField(columnname = IVPuiUser.DATEFORMAT_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 10, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 5, visibility = ColumnVisibility.visible)
	private String dateformat;
	/**
	 * @generated
	 */
	@PuiField(columnname = IVPuiUser.DISABLED_COLUMN, ispk = false, nullable = false, type = ColumnType.numeric, autoincrementable = false, maxlength = -1, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 6, visibility = ColumnVisibility.visible)
	private Integer disabled;
	/**
	 * @generated
	 */
	@PuiField(columnname = IVPuiUser.DISABLED_DATE_COLUMN, ispk = false, nullable = true, type = ColumnType.datetime, autoincrementable = false, maxlength = -1, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 7, visibility = ColumnVisibility.hidden)
	private java.time.Instant disableddate;
	/**
	 * @generated
	 */
	@PuiField(columnname = IVPuiUser.LAST_ACCESS_TIME_COLUMN, ispk = false, nullable = true, type = ColumnType.datetime, autoincrementable = false, maxlength = -1, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 8, visibility = ColumnVisibility.visible)
	private java.time.Instant lastaccesstime;
	/**
	 * @generated
	 */
	@PuiField(columnname = IVPuiUser.LAST_ACCESS_IP_COLUMN, ispk = false, nullable = true, type = ColumnType.text, autoincrementable = false, maxlength = 50, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 9, visibility = ColumnVisibility.visible)
	private String lastaccessip;

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
	public String getName() {
		return name;
	}

	/**
	 * @generated
	 */
	@Override
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @generated
	 */
	@Override
	public String getEmail() {
		return email;
	}

	/**
	 * @generated
	 */
	@Override
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @generated
	 */
	@Override
	public String getLanguage() {
		return language;
	}

	/**
	 * @generated
	 */
	@Override
	public void setLanguage(String language) {
		this.language = language;
	}

	/**
	 * @generated
	 */
	@Override
	public String getDateformat() {
		return dateformat;
	}

	/**
	 * @generated
	 */
	@Override
	public void setDateformat(String dateformat) {
		this.dateformat = dateformat;
	}

	/**
	 * @generated
	 */
	@Override
	public Integer getDisabled() {
		return disabled;
	}

	/**
	 * @generated
	 */
	@Override
	public void setDisabled(Integer disabled) {
		this.disabled = disabled;
	}

	/**
	 * @generated
	 */
	@Override
	public java.time.Instant getDisableddate() {
		return disableddate;
	}

	/**
	 * @generated
	 */
	@Override
	public void setDisableddate(java.time.Instant disableddate) {
		this.disableddate = disableddate;
	}

	/**
	 * @generated
	 */
	@Override
	public java.time.Instant getLastaccesstime() {
		return lastaccesstime;
	}

	/**
	 * @generated
	 */
	@Override
	public void setLastaccesstime(java.time.Instant lastaccesstime) {
		this.lastaccesstime = lastaccesstime;
	}

	/**
	 * @generated
	 */
	@Override
	public String getLastaccessip() {
		return lastaccessip;
	}

	/**
	 * @generated
	 */
	@Override
	public void setLastaccessip(String lastaccessip) {
		this.lastaccessip = lastaccessip;
	}
}

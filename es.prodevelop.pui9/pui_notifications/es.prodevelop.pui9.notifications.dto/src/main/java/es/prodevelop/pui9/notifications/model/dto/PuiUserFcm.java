package es.prodevelop.pui9.notifications.model.dto;

import es.prodevelop.pui9.annotations.PuiEntity;
import es.prodevelop.pui9.annotations.PuiField;
import es.prodevelop.pui9.enums.ColumnType;
import es.prodevelop.pui9.notifications.model.dto.interfaces.IPuiUserFcm;

/**
 * @generated
 */
@PuiEntity(tablename = "pui_user_fcm")
public class PuiUserFcm extends PuiUserFcmPk implements IPuiUserFcm {
	/**
	 * @generated
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * @generated
	 */
	@PuiField(columnname = IPuiUserFcm.USR_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	private String usr;
	/**
	 * @generated
	 */
	@PuiField(columnname = IPuiUserFcm.LAST_USE_COLUMN, ispk = false, nullable = false, type = ColumnType.datetime, autoincrementable = false, maxlength = -1, islang = false, isgeometry = false, issequence = false)
	private java.time.Instant lastuse;

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
	public java.time.Instant getLastuse() {
		return lastuse;
	}

	/**
	 * @generated
	 */
	@Override
	public void setLastuse(java.time.Instant lastuse) {
		this.lastuse = lastuse;
	}
}

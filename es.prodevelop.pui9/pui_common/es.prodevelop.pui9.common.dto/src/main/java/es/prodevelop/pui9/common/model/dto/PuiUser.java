package es.prodevelop.pui9.common.model.dto;

import java.util.ArrayList;
import java.util.List;

import es.prodevelop.pui9.annotations.PuiEntity;
import es.prodevelop.pui9.annotations.PuiField;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiProfile;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiUser;
import es.prodevelop.pui9.enums.ColumnType;

/**
 * @generated
 */
@PuiEntity(tablename = "pui_user")
public class PuiUser extends PuiUserPk implements IPuiUser {
	/**
	 * @generated
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * @generated
	 */
	@PuiField(columnname = IPuiUser.NAME_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 200, islang = false, isgeometry = false, issequence = false)
	private String name;
	/**
	 * @generated
	 */
	@PuiField(columnname = IPuiUser.PASSWORD_COLUMN, ispk = false, nullable = true, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	private String password;
	/**
	 * @generated
	 */
	@PuiField(columnname = IPuiUser.LANGUAGE_COLUMN, ispk = false, nullable = true, type = ColumnType.text, autoincrementable = false, maxlength = 2, islang = false, isgeometry = false, issequence = false)
	private String language;
	/**
	 * @generated
	 */
	@PuiField(columnname = IPuiUser.EMAIL_COLUMN, ispk = false, nullable = true, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	private String email;
	/**
	 * @generated
	 */
	@PuiField(columnname = IPuiUser.DISABLED_COLUMN, ispk = false, nullable = false, type = ColumnType.numeric, autoincrementable = false, maxlength = -1, islang = false, isgeometry = false, issequence = false)
	private Integer disabled = 0;
	/**
	 * @generated
	 */
	@PuiField(columnname = IPuiUser.DISABLED_DATE_COLUMN, ispk = false, nullable = true, type = ColumnType.datetime, autoincrementable = false, maxlength = -1, islang = false, isgeometry = false, issequence = false)
	private java.time.Instant disableddate;
	/**
	 * @generated
	 */
	@PuiField(columnname = IPuiUser.DATEFORMAT_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 10, islang = false, isgeometry = false, issequence = false)
	private String dateformat = "dd/MM/yyyy";
	/**
	 * @generated
	 */
	@PuiField(columnname = IPuiUser.RESET_PASSWORD_TOKEN_COLUMN, ispk = false, nullable = true, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	private String resetpasswordtoken;
	/**
	 * @generated
	 */
	@PuiField(columnname = IPuiUser.LAST_ACCESS_TIME_COLUMN, ispk = false, nullable = true, type = ColumnType.datetime, autoincrementable = false, maxlength = -1, islang = false, isgeometry = false, issequence = false)
	private java.time.Instant lastaccesstime;
	/**
	 * @generated
	 */
	@PuiField(columnname = IPuiUser.LAST_ACCESS_IP_COLUMN, ispk = false, nullable = true, type = ColumnType.text, autoincrementable = false, maxlength = 50, islang = false, isgeometry = false, issequence = false)
	private String lastaccessip;

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
	public String getPassword() {
		return password;
	}

	/**
	 * @generated
	 */
	@Override
	public void setPassword(String password) {
		this.password = password;
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
	public String getResetpasswordtoken() {
		return resetpasswordtoken;
	}

	/**
	 * @generated
	 */
	@Override
	public void setResetpasswordtoken(String resetpasswordtoken) {
		this.resetpasswordtoken = resetpasswordtoken;
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

	private List<IPuiProfile> profiles = new ArrayList<>();

	@Override
	public List<IPuiProfile> getProfiles() {
		return profiles;
	}

	@Override
	public void setProfiles(List<IPuiProfile> profiles) {
		this.profiles = profiles;
	}
}

package es.prodevelop.pui9.common.model.dto;

import es.prodevelop.pui9.annotations.PuiField;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiUserProfilePk;
import es.prodevelop.pui9.enums.ColumnType;
import es.prodevelop.pui9.model.dto.AbstractTableDto;
import es.prodevelop.pui9.utils.PuiObjectUtils;

/**
 * @generated
 */
public class PuiUserProfilePk extends AbstractTableDto implements IPuiUserProfilePk {
	/**
	 * @generated
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * @generated
	 */
	@PuiField(columnname = IPuiUserProfilePk.USR_COLUMN, ispk = true, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	private String usr;
	/**
	 * @generated
	 */
	@PuiField(columnname = IPuiUserProfilePk.PROFILE_COLUMN, ispk = true, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	private String profile;

	/**
	 * @generated
	 */
	public PuiUserProfilePk() {
	}

	/**
	 * @generated
	 */
	public PuiUserProfilePk(String profile, String usr) {
		this.profile = profile;
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
	public String getProfile() {
		return profile;
	}

	/**
	 * @generated
	 */
	@Override
	public void setProfile(String profile) {
		this.profile = profile;
	}

	/**
	 * @generated
	 */
	@Override
	@SuppressWarnings("unchecked")
	public PuiUserProfilePk createPk() {
		PuiUserProfilePk pk = new PuiUserProfilePk();
		PuiObjectUtils.copyProperties(pk, this);
		return pk;
	}
}

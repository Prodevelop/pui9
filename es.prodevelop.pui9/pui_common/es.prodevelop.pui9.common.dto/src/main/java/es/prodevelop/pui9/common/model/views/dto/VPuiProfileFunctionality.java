package es.prodevelop.pui9.common.model.views.dto;

import es.prodevelop.pui9.annotations.PuiEntity;
import es.prodevelop.pui9.annotations.PuiField;
import es.prodevelop.pui9.annotations.PuiViewColumn;
import es.prodevelop.pui9.common.model.views.dto.interfaces.IVPuiProfileFunctionality;
import es.prodevelop.pui9.enums.ColumnType;
import es.prodevelop.pui9.enums.ColumnVisibility;
import es.prodevelop.pui9.model.dto.AbstractViewDto;

/**
 * @generated
 */
@PuiEntity(tablename = "v_pui_profile_functionality")
public class VPuiProfileFunctionality extends AbstractViewDto implements IVPuiProfileFunctionality {
	/**
	 * @generated
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * @generated
	 */
	@PuiField(columnname = IVPuiProfileFunctionality.PROFILE_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 1, visibility = ColumnVisibility.visible)
	private String profile;
	/**
	 * @generated
	 */
	@PuiField(columnname = IVPuiProfileFunctionality.PROFILE_NAME_COLUMN, ispk = false, nullable = true, type = ColumnType.text, autoincrementable = false, maxlength = 200, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 2, visibility = ColumnVisibility.visible)
	private String profilename;
	/**
	 * @generated
	 */
	@PuiField(columnname = IVPuiProfileFunctionality.FUNCTIONALITY_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 3, visibility = ColumnVisibility.visible)
	private String functionality;
	/**
	 * @generated
	 */
	@PuiField(columnname = IVPuiProfileFunctionality.LANG_COLUMN, ispk = false, nullable = true, type = ColumnType.text, autoincrementable = false, maxlength = 2, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 4, visibility = ColumnVisibility.completelyhidden)
	private String lang;

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
	public String getProfilename() {
		return profilename;
	}

	/**
	 * @generated
	 */
	@Override
	public void setProfilename(String profilename) {
		this.profilename = profilename;
	}

	/**
	 * @generated
	 */
	@Override
	public String getFunctionality() {
		return functionality;
	}

	/**
	 * @generated
	 */
	@Override
	public void setFunctionality(String functionality) {
		this.functionality = functionality;
	}

	/**
	 * @generated
	 */
	@Override
	public String getLang() {
		return lang;
	}

	/**
	 * @generated
	 */
	@Override
	public void setLang(String lang) {
		this.lang = lang;
	}
}

package es.prodevelop.pui9.common.model.dto;

import es.prodevelop.pui9.annotations.PuiField;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiProfileFunctionalityPk;
import es.prodevelop.pui9.enums.ColumnType;
import es.prodevelop.pui9.model.dto.AbstractTableDto;
import es.prodevelop.pui9.utils.PuiObjectUtils;

/**
 * @generated
 */
public class PuiProfileFunctionalityPk extends AbstractTableDto implements IPuiProfileFunctionalityPk {
	/**
	 * @generated
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * @generated
	 */
	@PuiField(columnname = IPuiProfileFunctionalityPk.PROFILE_COLUMN, ispk = true, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	private String profile;
	/**
	 * @generated
	 */
	@PuiField(columnname = IPuiProfileFunctionalityPk.FUNCTIONALITY_COLUMN, ispk = true, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	private String functionality;

	/**
	 * @generated
	 */
	public PuiProfileFunctionalityPk() {
	}

	/**
	 * @generated
	 */
	public PuiProfileFunctionalityPk(String functionality, String profile) {
		this.functionality = functionality;
		this.profile = profile;
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
	@SuppressWarnings("unchecked")
	public PuiProfileFunctionalityPk createPk() {
		PuiProfileFunctionalityPk pk = new PuiProfileFunctionalityPk();
		PuiObjectUtils.copyProperties(pk, this);
		return pk;
	}
}

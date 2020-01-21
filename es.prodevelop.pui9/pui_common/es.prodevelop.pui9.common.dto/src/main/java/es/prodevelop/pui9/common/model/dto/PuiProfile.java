package es.prodevelop.pui9.common.model.dto;

import java.util.ArrayList;
import java.util.List;

import es.prodevelop.pui9.annotations.PuiEntity;
import es.prodevelop.pui9.annotations.PuiField;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiFunctionality;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiProfile;
import es.prodevelop.pui9.enums.ColumnType;

/**
 * @generated
 */
@PuiEntity(tablename = "pui_profile", tabletranslationname = "pui_profile_tra")
public class PuiProfile extends PuiProfilePk implements IPuiProfile {
	/**
	 * @generated
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * @generated
	 */
	@PuiField(columnname = IPuiProfile.LANG_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 2, islang = true, isgeometry = false, issequence = false)
	private String lang;
	/**
	 * @generated
	 */
	@PuiField(columnname = IPuiProfile.LANG_STATUS_COLUMN, ispk = false, nullable = false, type = ColumnType.numeric, autoincrementable = false, maxlength = -1, islang = true, isgeometry = false, issequence = false)
	private Integer langstatus = 0;
	/**
	 * @generated
	 */
	@PuiField(columnname = IPuiProfile.NAME_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 200, islang = true, isgeometry = false, issequence = false)
	private String name;

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

	/**
	 * @generated
	 */
	@Override
	public Integer getLangstatus() {
		return langstatus;
	}

	/**
	 * @generated
	 */
	@Override
	public void setLangstatus(Integer langstatus) {
		this.langstatus = langstatus;
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

	private List<IPuiFunctionality> functionalities = new ArrayList<>();

	@Override
	public List<IPuiFunctionality> getFunctionalities() {
		return functionalities;
	}

	@Override
	public void setFunctionalities(List<IPuiFunctionality> functionalities) {
		this.functionalities = functionalities;
	}
}

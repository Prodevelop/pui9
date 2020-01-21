package es.prodevelop.pui9.common.model.dto;

import es.prodevelop.pui9.annotations.PuiEntity;
import es.prodevelop.pui9.annotations.PuiField;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiProfileTra;
import es.prodevelop.pui9.enums.ColumnType;

/**
 * @generated
 */
@PuiEntity(tablename = "pui_profile_tra")
public class PuiProfileTra extends PuiProfileTraPk implements IPuiProfileTra {
	/**
	 * @generated
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * @generated
	 */
	@PuiField(columnname = IPuiProfileTra.LANG_STATUS_COLUMN, ispk = false, nullable = false, type = ColumnType.numeric, autoincrementable = false, maxlength = -1, islang = false, isgeometry = false, issequence = false)
	private Integer langstatus = 0;
	/**
	 * @generated
	 */
	@PuiField(columnname = IPuiProfileTra.NAME_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 200, islang = false, isgeometry = false, issequence = false)
	private String name;

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
}

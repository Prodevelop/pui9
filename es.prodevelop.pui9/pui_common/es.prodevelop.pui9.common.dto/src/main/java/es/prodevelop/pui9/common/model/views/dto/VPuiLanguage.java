package es.prodevelop.pui9.common.model.views.dto;

import es.prodevelop.pui9.annotations.PuiEntity;
import es.prodevelop.pui9.annotations.PuiField;
import es.prodevelop.pui9.annotations.PuiViewColumn;
import es.prodevelop.pui9.common.model.views.dto.interfaces.IVPuiLanguage;
import es.prodevelop.pui9.enums.ColumnType;
import es.prodevelop.pui9.enums.ColumnVisibility;
import es.prodevelop.pui9.model.dto.AbstractViewDto;

/**
 * @generated
 */
@PuiEntity(tablename = "v_pui_language")
public class VPuiLanguage extends AbstractViewDto implements IVPuiLanguage {
	/**
	 * @generated
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * @generated
	 */
	@PuiField(columnname = IVPuiLanguage.ISOCODE_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 2, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 1, visibility = ColumnVisibility.visible)
	private String isocode;
	/**
	 * @generated
	 */
	@PuiField(columnname = IVPuiLanguage.NAME_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 2, visibility = ColumnVisibility.visible)
	private String name;
	/**
	 * @generated
	 */
	@PuiField(columnname = IVPuiLanguage.ISDEFAULT_COLUMN, ispk = false, nullable = false, type = ColumnType.numeric, autoincrementable = false, maxlength = -1, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 3, visibility = ColumnVisibility.visible)
	private Integer isdefault;

	/**
	 * @generated
	 */
	@Override
	public String getIsocode() {
		return isocode;
	}

	/**
	 * @generated
	 */
	@Override
	public void setIsocode(String isocode) {
		this.isocode = isocode;
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
	public Integer getIsdefault() {
		return isdefault;
	}

	/**
	 * @generated
	 */
	@Override
	public void setIsdefault(Integer isdefault) {
		this.isdefault = isdefault;
	}
}

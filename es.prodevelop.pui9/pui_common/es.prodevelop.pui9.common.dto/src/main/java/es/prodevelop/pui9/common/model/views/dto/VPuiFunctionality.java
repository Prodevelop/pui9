package es.prodevelop.pui9.common.model.views.dto;

import es.prodevelop.pui9.annotations.PuiEntity;
import es.prodevelop.pui9.annotations.PuiField;
import es.prodevelop.pui9.annotations.PuiViewColumn;
import es.prodevelop.pui9.common.model.views.dto.interfaces.IVPuiFunctionality;
import es.prodevelop.pui9.enums.ColumnType;
import es.prodevelop.pui9.enums.ColumnVisibility;
import es.prodevelop.pui9.model.dto.AbstractViewDto;

/**
 * @generated
 */
@PuiEntity(tablename = "v_pui_functionality")
public class VPuiFunctionality extends AbstractViewDto implements IVPuiFunctionality {
	/**
	 * @generated
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * @generated
	 */
	@PuiField(columnname = IVPuiFunctionality.FUNCTIONALITY_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 1, visibility = ColumnVisibility.visible)
	private String functionality;
	/**
	 * @generated
	 */
	@PuiField(columnname = IVPuiFunctionality.NAME_COLUMN, ispk = false, nullable = true, type = ColumnType.text, autoincrementable = false, maxlength = 200, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 2, visibility = ColumnVisibility.visible)
	private String name;
	/**
	 * @generated
	 */
	@PuiField(columnname = IVPuiFunctionality.SUBSYSTEM_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 3, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 3, visibility = ColumnVisibility.visible)
	private String subsystem;
	/**
	 * @generated
	 */
	@PuiField(columnname = IVPuiFunctionality.SUBSYSTEM_NAME_COLUMN, ispk = false, nullable = true, type = ColumnType.text, autoincrementable = false, maxlength = 200, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 4, visibility = ColumnVisibility.visible)
	private String subsystemname;
	/**
	 * @generated
	 */
	@PuiField(columnname = IVPuiFunctionality.LANG_COLUMN, ispk = false, nullable = true, type = ColumnType.text, autoincrementable = false, maxlength = 2, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 5, visibility = ColumnVisibility.completelyhidden)
	private String lang;

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
	public String getSubsystem() {
		return subsystem;
	}

	/**
	 * @generated
	 */
	@Override
	public void setSubsystem(String subsystem) {
		this.subsystem = subsystem;
	}

	/**
	 * @generated
	 */
	@Override
	public String getSubsystemname() {
		return subsystemname;
	}

	/**
	 * @generated
	 */
	@Override
	public void setSubsystemname(String subsystemname) {
		this.subsystemname = subsystemname;
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

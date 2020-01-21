package es.prodevelop.pui9.common.model.dto;

import java.util.ArrayList;
import java.util.List;

import es.prodevelop.pui9.annotations.PuiEntity;
import es.prodevelop.pui9.annotations.PuiField;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiMenu;
import es.prodevelop.pui9.enums.ColumnType;

/**
 * @generated
 */
@PuiEntity(tablename = "pui_menu")
public class PuiMenu extends PuiMenuPk implements IPuiMenu {
	/**
	 * @generated
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * @generated
	 */
	@PuiField(columnname = IPuiMenu.PARENT_COLUMN, ispk = false, nullable = true, type = ColumnType.numeric, autoincrementable = false, maxlength = -1, islang = false, isgeometry = false, issequence = false)
	private Integer parent;
	/**
	 * @generated
	 */
	@PuiField(columnname = IPuiMenu.MODEL_COLUMN, ispk = false, nullable = true, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	private String model;
	/**
	 * @generated
	 */
	@PuiField(columnname = IPuiMenu.COMPONENT_COLUMN, ispk = false, nullable = true, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	private String component;
	/**
	 * @generated
	 */
	@PuiField(columnname = IPuiMenu.FUNCTIONALITY_COLUMN, ispk = false, nullable = true, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	private String functionality;
	/**
	 * @generated
	 */
	@PuiField(columnname = IPuiMenu.LABEL_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	private String label;
	/**
	 * @generated
	 */
	@PuiField(columnname = IPuiMenu.ICON_LABEL_COLUMN, ispk = false, nullable = true, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	private String iconlabel;

	/**
	 * @generated
	 */
	@Override
	public Integer getParent() {
		return parent;
	}

	/**
	 * @generated
	 */
	@Override
	public void setParent(Integer parent) {
		this.parent = parent;
	}

	/**
	 * @generated
	 */
	@Override
	public String getModel() {
		return model;
	}

	/**
	 * @generated
	 */
	@Override
	public void setModel(String model) {
		this.model = model;
	}

	/**
	 * @generated
	 */
	@Override
	public String getComponent() {
		return component;
	}

	/**
	 * @generated
	 */
	@Override
	public void setComponent(String component) {
		this.component = component;
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
	public String getLabel() {
		return label;
	}

	/**
	 * @generated
	 */
	@Override
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * @generated
	 */
	@Override
	public String getIconlabel() {
		return iconlabel;
	}

	/**
	 * @generated
	 */
	@Override
	public void setIconlabel(String iconlabel) {
		this.iconlabel = iconlabel;
	}

	private List<IPuiMenu> children = new ArrayList<>();

	@Override
	public List<IPuiMenu> getChildren() {
		return children;
	}

	@Override
	public void setChildren(List<IPuiMenu> children) {
		this.children = children;
	}
}

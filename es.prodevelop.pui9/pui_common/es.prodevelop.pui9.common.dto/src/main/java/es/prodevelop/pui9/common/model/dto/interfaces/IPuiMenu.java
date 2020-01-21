package es.prodevelop.pui9.common.model.dto.interfaces;

import java.util.List;

/**
 * @generated
 */
public interface IPuiMenu extends IPuiMenuPk {
	/**
	 * @generated
	 */
	String PARENT_COLUMN = "parent";
	/**
	 * @generated
	 */
	String PARENT_FIELD = "parent";
	/**
	 * @generated
	 */
	String MODEL_COLUMN = "model";
	/**
	 * @generated
	 */
	String MODEL_FIELD = "model";
	/**
	 * @generated
	 */
	String COMPONENT_COLUMN = "component";
	/**
	 * @generated
	 */
	String COMPONENT_FIELD = "component";
	/**
	 * @generated
	 */
	String FUNCTIONALITY_COLUMN = "functionality";
	/**
	 * @generated
	 */
	String FUNCTIONALITY_FIELD = "functionality";
	/**
	 * @generated
	 */
	String LABEL_COLUMN = "label";
	/**
	 * @generated
	 */
	String LABEL_FIELD = "label";
	/**
	 * @generated
	 */
	String ICON_LABEL_COLUMN = "icon_label";
	/**
	 * @generated
	 */
	String ICON_LABEL_FIELD = "iconlabel";

	/**
	 * @generated
	 */
	Integer getParent();

	/**
	 * @generated
	 */
	void setParent(Integer parent);

	/**
	 * @generated
	 */
	String getModel();

	/**
	 * @generated
	 */
	void setModel(String model);

	/**
	 * @generated
	 */
	String getComponent();

	/**
	 * @generated
	 */
	void setComponent(String component);

	/**
	 * @generated
	 */
	String getFunctionality();

	/**
	 * @generated
	 */
	void setFunctionality(String functionality);

	/**
	 * @generated
	 */
	String getLabel();

	/**
	 * @generated
	 */
	void setLabel(String label);

	/**
	 * @generated
	 */
	String getIconlabel();

	/**
	 * @generated
	 */
	void setIconlabel(String iconlabel);

	List<IPuiMenu> getChildren();

	void setChildren(List<IPuiMenu> children);
}

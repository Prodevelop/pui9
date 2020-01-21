package es.prodevelop.pui9.common.model.dto.interfaces;

/**
 * @generated
 */
public interface IPuiVariable extends IPuiVariablePk {
	String VARIABLE_WITH_NO_VALUE = "-";
	/**
	 * @generated
	 */
	String VALUE_COLUMN = "value";
	/**
	 * @generated
	 */
	String VALUE_FIELD = "value";
	/**
	 * @generated
	 */
	String DESCRIPTION_COLUMN = "description";
	/**
	 * @generated
	 */
	String DESCRIPTION_FIELD = "description";

	/**
	 * @generated
	 */
	String getValue();

	/**
	 * @generated
	 */
	void setValue(String value);

	/**
	 * @generated
	 */
	String getDescription();

	/**
	 * @generated
	 */
	void setDescription(String description);
}

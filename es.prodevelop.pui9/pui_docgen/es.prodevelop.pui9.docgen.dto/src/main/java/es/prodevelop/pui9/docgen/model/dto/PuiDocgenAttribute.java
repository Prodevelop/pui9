package es.prodevelop.pui9.docgen.model.dto;

import es.prodevelop.pui9.annotations.PuiEntity;
import es.prodevelop.pui9.annotations.PuiField;
import es.prodevelop.pui9.docgen.model.dto.interfaces.IPuiDocgenAttribute;
import es.prodevelop.pui9.enums.ColumnType;

/**
 * @generated
 */
@PuiEntity(tablename = "pui_docgen_attribute")
public class PuiDocgenAttribute extends PuiDocgenAttributePk implements IPuiDocgenAttribute {
	/**
	 * @generated
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * @generated
	 */
	@PuiField(columnname = IPuiDocgenAttribute.LABEL_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	private String label;
	/**
	 * @generated
	 */
	@PuiField(columnname = IPuiDocgenAttribute.VALUE_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 500, islang = false, isgeometry = false, issequence = false)
	private String value;

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
	public String getValue() {
		return value;
	}

	/**
	 * @generated
	 */
	@Override
	public void setValue(String value) {
		this.value = value;
	}
}

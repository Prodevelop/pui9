package es.prodevelop.pui9.model.configuration;

import es.prodevelop.pui9.utils.IPuiObject;

public class FilterComboRelatedCombo implements IPuiObject {

	private static final long serialVersionUID = 1L;

	private String id;
	private String toColumn;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getToColumn() {
		return toColumn;
	}

	public void setToColumn(String toColumn) {
		this.toColumn = toColumn;
	}

}

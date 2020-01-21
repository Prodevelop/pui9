package es.prodevelop.pui9.docgen.dto;

import es.prodevelop.pui9.utils.IPuiObject;

public class MappingValueDto implements IPuiObject {
	private static final long serialVersionUID = 1L;

	private String field;
	private String val;

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getVal() {
		return val;
	}

	public void setVal(String val) {
		this.val = val;
	}

	@Override
	public String toString() {
		return field + "::'" + val + "'";
	}

}

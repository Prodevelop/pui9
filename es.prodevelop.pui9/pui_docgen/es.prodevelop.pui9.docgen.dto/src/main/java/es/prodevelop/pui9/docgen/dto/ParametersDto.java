package es.prodevelop.pui9.docgen.dto;

import es.prodevelop.pui9.filter.FilterRuleOperation;
import es.prodevelop.pui9.utils.IPuiObject;

public class ParametersDto implements IPuiObject {
	private static final long serialVersionUID = 1L;

	private String field;
	private FilterRuleOperation op;

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public FilterRuleOperation getOp() {
		return op;
	}

	public void setOp(FilterRuleOperation op) {
		this.op = op;
	}

	@Override
	public String toString() {
		return field + " " + op;
	}

}

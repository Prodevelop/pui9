package es.prodevelop.pui9.filter;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * This class represents a Rule in a Filter. A simple Rule is composed by the
 * field agains it's operating, the operation type and the value to compare with
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
@ApiModel(value = "Rules description")
public class FilterRule implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "Field", position = -1, required = true)
	private String field;

	@ApiModelProperty(value = "Operation", position = -1, required = true)
	private FilterRuleOperation op;

	@ApiModelProperty(value = "Value", position = -1, required = true)
	private Object data;

	public FilterRule() {
		this(null, null, null);
	}

	public FilterRule(String field, FilterRuleOperation op, Object data) {
		this.field = field;
		this.op = op;
		this.data = data;
	}

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

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "(" + field + " " + op + " " + data + ")";
	}

}

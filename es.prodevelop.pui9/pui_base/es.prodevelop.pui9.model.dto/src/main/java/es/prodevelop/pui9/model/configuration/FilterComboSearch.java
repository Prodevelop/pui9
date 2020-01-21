package es.prodevelop.pui9.model.configuration;

import java.util.Map;

import es.prodevelop.pui9.order.OrderDirection;
import es.prodevelop.pui9.utils.IPuiObject;

public class FilterComboSearch implements IPuiObject {

	private static final long serialVersionUID = 1L;

	private String model;
	private String value;
	private String text;
	private Map<String, OrderDirection> order;
	private String related;

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Map<String, OrderDirection> getOrder() {
		return order;
	}

	public void setOrder(Map<String, OrderDirection> order) {
		this.order = order;
	}

	public String getRelated() {
		return related;
	}

	public void setRelated(String related) {
		this.related = related;
	}

}

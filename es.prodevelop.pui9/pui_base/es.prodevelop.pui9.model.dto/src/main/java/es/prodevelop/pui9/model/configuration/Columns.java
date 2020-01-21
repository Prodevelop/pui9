package es.prodevelop.pui9.model.configuration;

import es.prodevelop.pui9.enums.ColumnVisibility;
import es.prodevelop.pui9.utils.IPuiObject;

public class Columns implements IPuiObject {

	private static final long serialVersionUID = 1L;

	private String name;
	private ColumnVisibility visibility;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ColumnVisibility getVisibility() {
		return visibility;
	}

	public void setVisibility(ColumnVisibility visibility) {
		this.visibility = visibility;
	}

}

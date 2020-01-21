package es.prodevelop.pui9.docgen.dto;

import es.prodevelop.pui9.utils.IPuiObject;

public class MappingDto implements IPuiObject {
	private static final long serialVersionUID = 1L;

	public static final String ORIGIN_VIEW = "V";
	public static final String ORIGIN_USER = "U";
	public static final String ORIGIN_SYSTEM = "S";
	public static final String ORIGIN_TABLE = "T";

	private String field;
	private String tag;
	private String origin = ORIGIN_VIEW;

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	@Override
	public String toString() {
		return tag + "::" + field + " (" + (origin != null ? origin : "null") + ")";
	}

}

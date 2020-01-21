package es.prodevelop.pui9.publishaudit.dto;

import java.util.ArrayList;
import java.util.List;

public class PublishField {

	private String fieldname;
	private List<PublishFieldValue> values;

	public PublishField(String fieldname) {
		this.fieldname = fieldname;
		this.values = new ArrayList<>();
	}

	public String getFieldname() {
		return fieldname;
	}

	public List<PublishFieldValue> getValues() {
		return values;
	}

}

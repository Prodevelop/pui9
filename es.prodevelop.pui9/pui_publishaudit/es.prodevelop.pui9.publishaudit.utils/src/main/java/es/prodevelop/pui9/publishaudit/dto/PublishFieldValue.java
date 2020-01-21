package es.prodevelop.pui9.publishaudit.dto;

public class PublishFieldValue {

	private String oldvalue;
	private String newvalue;

	public PublishFieldValue(String oldvalue, String newvalue) {
		this.oldvalue = oldvalue;
		this.newvalue = newvalue;
	}

	public String getOldvalue() {
		return oldvalue;
	}

	public String getNewvalue() {
		return newvalue;
	}

}
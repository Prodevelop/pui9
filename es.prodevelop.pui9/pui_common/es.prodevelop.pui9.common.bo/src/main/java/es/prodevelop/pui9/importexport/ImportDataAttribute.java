package es.prodevelop.pui9.importexport;

import es.prodevelop.pui9.utils.IPuiObject;

public class ImportDataAttribute implements IPuiObject {

	private static final long serialVersionUID = 1L;

	private Object value;
	private Object oldValue;
	private ImportDataAttributeStatus status;

	public ImportDataAttribute(Object value) {
		this(value, false);
	}

	public ImportDataAttribute(Object value, boolean hasError) {
		this.value = value;
		this.oldValue = null;
		this.status = hasError ? ImportDataAttributeStatus.ERROR : ImportDataAttributeStatus.UNMODIFIED;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public Object getOldValue() {
		return oldValue;
	}

	public void setOldValue(Object oldValue) {
		this.oldValue = oldValue;
	}

	public ImportDataAttributeStatus getStatus() {
		return status;
	}

	public void setStatus(ImportDataAttributeStatus status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "name (" + status + ")";
	}

}

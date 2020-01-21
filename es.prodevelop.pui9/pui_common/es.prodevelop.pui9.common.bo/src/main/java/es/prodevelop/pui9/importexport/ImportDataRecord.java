package es.prodevelop.pui9.importexport;

import java.util.HashMap;
import java.util.Map;

import es.prodevelop.pui9.utils.IPuiObject;

public class ImportDataRecord implements IPuiObject {

	private static final long serialVersionUID = 1L;

	private ImportDataRecordStatus status;
	private Map<String, ImportDataAttribute> attributes;

	public ImportDataRecord() {
		this.status = ImportDataRecordStatus.UNMODIFIED;
		this.attributes = new HashMap<>();
	}

	public ImportDataRecordStatus getStatus() {
		return status;
	}

	public void setStatus(ImportDataRecordStatus status) {
		this.status = status;
	}

	public Map<String, ImportDataAttribute> getAttributes() {
		return attributes;
	}

	public void addAttribute(String name, ImportDataAttribute attribute) {
		attributes.put(name, attribute);
	}

	@Override
	public String toString() {
		return attributes.size() + " attributes (" + status + ")";
	}

}

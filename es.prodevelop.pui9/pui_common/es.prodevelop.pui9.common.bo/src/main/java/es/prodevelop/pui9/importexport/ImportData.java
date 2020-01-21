package es.prodevelop.pui9.importexport;

import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.prodevelop.pui9.utils.IPuiObject;

public class ImportData implements IPuiObject {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private Instant importTime;
	private String user;
	private String model;
	private String language;
	private List<String> pks;
	private List<String> columns;
	private List<String> columnTitles;
	private Long totalRecords = 0L;
	private Long newRecords = 0L;
	private Long newWithErrorsRecords = 0L;
	private Long modifiedRecords = 0L;
	private Long errorRecords = 0L;
	private Long unmodifiedRecords = 0L;
	private Map<String, ImportDataRecord> records;

	public ImportData(String user, String model, String language, List<String> pks, List<String> columns,
			List<String> columnTitles) {
		this.importTime = Instant.now();
		this.user = user;
		this.model = model;
		this.language = language;
		this.pks = pks != null ? pks : Collections.emptyList();
		this.columns = columns != null ? columns : Collections.emptyList();
		this.columnTitles = columnTitles != null ? columnTitles : columns;
		this.records = new HashMap<>();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Instant getImportTime() {
		return importTime;
	}

	public String getUser() {
		return user;
	}

	public String getModel() {
		return model;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public List<String> getPks() {
		return pks;
	}

	public List<String> getColumns() {
		return columns;
	}

	public List<String> getColumnTitles() {
		return columnTitles;
	}

	public Long getTotalRecords() {
		return totalRecords;
	}

	public Long getNewRecords() {
		return newRecords;
	}

	public void addNewRecord() {
		this.newRecords++;
		this.totalRecords++;
	}

	public Long getNewWithErrorsRecords() {
		return newWithErrorsRecords;
	}

	public void addNewWithErrorsRecord() {
		this.newWithErrorsRecords++;
		this.totalRecords++;
	}

	public Long getModifiedRecords() {
		return modifiedRecords;
	}

	public void addModifiedRecord() {
		this.modifiedRecords++;
		this.totalRecords++;
	}

	public Long getErrorRecords() {
		return errorRecords;
	}

	public void addErrorRecord() {
		this.errorRecords++;
		this.totalRecords++;
	}

	public Long getUnmodifiedRecords() {
		return unmodifiedRecords;
	}

	public void addUnmodifiedRecord() {
		this.unmodifiedRecords++;
		this.totalRecords++;
	}

	public Map<String, ImportDataRecord> getRecords() {
		return records;
	}

	public void addRecord(String pk, ImportDataRecord record) {
		records.put(pk, record);
	}

}

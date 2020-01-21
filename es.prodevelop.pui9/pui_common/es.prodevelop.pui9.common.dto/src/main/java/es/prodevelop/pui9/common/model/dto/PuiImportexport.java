package es.prodevelop.pui9.common.model.dto;

import es.prodevelop.pui9.annotations.PuiEntity;
import es.prodevelop.pui9.annotations.PuiField;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiImportexport;
import es.prodevelop.pui9.enums.ColumnType;

/**
 * @generated
 */
@PuiEntity(tablename = "pui_importexport")
public class PuiImportexport extends PuiImportexportPk implements IPuiImportexport {
	/**
	 * @generated
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * @generated
	 */
	@PuiField(columnname = IPuiImportexport.MODEL_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	private String model;
	/**
	 * @generated
	 */
	@PuiField(columnname = IPuiImportexport.USR_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	private String usr;
	/**
	 * @generated
	 */
	@PuiField(columnname = IPuiImportexport.DATETIME_COLUMN, ispk = false, nullable = false, type = ColumnType.datetime, autoincrementable = false, maxlength = -1, islang = false, isgeometry = false, issequence = false)
	private java.time.Instant datetime;
	/**
	 * @generated
	 */
	@PuiField(columnname = IPuiImportexport.FILENAME_CSV_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	private String filenamecsv;
	/**
	 * @generated
	 */
	@PuiField(columnname = IPuiImportexport.FILENAME_JSON_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	private String filenamejson;
	/**
	 * @generated
	 */
	@PuiField(columnname = IPuiImportexport.EXECUTED_COLUMN, ispk = false, nullable = false, type = ColumnType.numeric, autoincrementable = false, maxlength = -1, islang = false, isgeometry = false, issequence = false)
	private Integer executed = 0;

	/**
	 * @generated
	 */
	@Override
	public String getModel() {
		return model;
	}

	/**
	 * @generated
	 */
	@Override
	public void setModel(String model) {
		this.model = model;
	}

	/**
	 * @generated
	 */
	@Override
	public String getUsr() {
		return usr;
	}

	/**
	 * @generated
	 */
	@Override
	public void setUsr(String usr) {
		this.usr = usr;
	}

	/**
	 * @generated
	 */
	@Override
	public java.time.Instant getDatetime() {
		return datetime;
	}

	/**
	 * @generated
	 */
	@Override
	public void setDatetime(java.time.Instant datetime) {
		this.datetime = datetime;
	}

	/**
	 * @generated
	 */
	@Override
	public String getFilenamecsv() {
		return filenamecsv;
	}

	/**
	 * @generated
	 */
	@Override
	public void setFilenamecsv(String filenamecsv) {
		this.filenamecsv = filenamecsv;
	}

	/**
	 * @generated
	 */
	@Override
	public String getFilenamejson() {
		return filenamejson;
	}

	/**
	 * @generated
	 */
	@Override
	public void setFilenamejson(String filenamejson) {
		this.filenamejson = filenamejson;
	}

	/**
	 * @generated
	 */
	@Override
	public Integer getExecuted() {
		return executed;
	}

	/**
	 * @generated
	 */
	@Override
	public void setExecuted(Integer executed) {
		this.executed = executed;
	}
}

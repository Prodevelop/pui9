package es.prodevelop.pui9.documents.model.dto;

import es.prodevelop.pui9.annotations.PuiEntity;
import es.prodevelop.pui9.annotations.PuiField;
import es.prodevelop.pui9.documents.model.dto.interfaces.IPuiDocument;
import es.prodevelop.pui9.enums.ColumnType;

/**
 * @generated
 */
@PuiEntity(tablename = "pui_document")
public class PuiDocument extends PuiDocumentPk implements IPuiDocument {
	/**
	 * @generated
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * @generated
	 */
	@PuiField(columnname = IPuiDocument.MODEL_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	private String model;
	/**
	 * @generated
	 */
	@PuiField(columnname = IPuiDocument.PK_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	private String pk;
	/**
	 * @generated
	 */
	@PuiField(columnname = IPuiDocument.LANGUAGE_COLUMN, ispk = false, nullable = true, type = ColumnType.text, autoincrementable = false, maxlength = 2, islang = false, isgeometry = false, issequence = false)
	private String language;
	/**
	 * @generated
	 */
	@PuiField(columnname = IPuiDocument.DESCRIPTION_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	private String description;
	/**
	 * @generated
	 */
	@PuiField(columnname = IPuiDocument.FILENAME_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	private String filename;
	/**
	 * @generated
	 */
	@PuiField(columnname = IPuiDocument.FILENAME_ORIG_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	private String filenameorig;
	/**
	 * @generated
	 */
	@PuiField(columnname = IPuiDocument.ROLE_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	private String role;
	/**
	 * @generated
	 */
	@PuiField(columnname = IPuiDocument.THUMBNAILS_COLUMN, ispk = false, nullable = true, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	private String thumbnails;
	/**
	 * @generated
	 */
	@PuiField(columnname = IPuiDocument.DATETIME_COLUMN, ispk = false, nullable = false, type = ColumnType.datetime, autoincrementable = false, maxlength = -1, islang = false, isgeometry = false, issequence = false)
	private java.time.Instant datetime;

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
	public String getPk() {
		return pk;
	}

	/**
	 * @generated
	 */
	@Override
	public void setPk(String pk) {
		this.pk = pk;
	}

	/**
	 * @generated
	 */
	@Override
	public String getLanguage() {
		return language;
	}

	/**
	 * @generated
	 */
	@Override
	public void setLanguage(String language) {
		this.language = language;
	}

	/**
	 * @generated
	 */
	@Override
	public String getDescription() {
		return description;
	}

	/**
	 * @generated
	 */
	@Override
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @generated
	 */
	@Override
	public String getFilename() {
		return filename;
	}

	/**
	 * @generated
	 */
	@Override
	public void setFilename(String filename) {
		this.filename = filename;
	}

	/**
	 * @generated
	 */
	@Override
	public String getFilenameorig() {
		return filenameorig;
	}

	/**
	 * @generated
	 */
	@Override
	public void setFilenameorig(String filenameorig) {
		this.filenameorig = filenameorig;
	}

	/**
	 * @generated
	 */
	@Override
	public String getRole() {
		return role;
	}

	/**
	 * @generated
	 */
	@Override
	public void setRole(String role) {
		this.role = role;
	}

	/**
	 * @generated
	 */
	@Override
	public String getThumbnails() {
		return thumbnails;
	}

	/**
	 * @generated
	 */
	@Override
	public void setThumbnails(String thumbnails) {
		this.thumbnails = thumbnails;
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

	private String url;

	@Override
	public String getUrl() {
		return url;
	}

	@Override
	public void setUrl(String url) {
		this.url = url;
	}
}

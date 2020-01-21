package es.prodevelop.pui9.documents.model.views.dto;

import es.prodevelop.pui9.annotations.PuiEntity;
import es.prodevelop.pui9.annotations.PuiField;
import es.prodevelop.pui9.annotations.PuiViewColumn;
import es.prodevelop.pui9.documents.model.views.dto.interfaces.IVPuiDocument;
import es.prodevelop.pui9.enums.ColumnType;
import es.prodevelop.pui9.enums.ColumnVisibility;
import es.prodevelop.pui9.model.dto.AbstractViewDto;

/**
 * @generated
 */
@PuiEntity(tablename = "v_pui_document")
public class VPuiDocument extends AbstractViewDto implements IVPuiDocument {
	/**
	 * @generated
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * @generated
	 */
	@PuiField(columnname = IVPuiDocument.ID_COLUMN, ispk = false, nullable = false, type = ColumnType.numeric, autoincrementable = false, maxlength = -1, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 1, visibility = ColumnVisibility.visible)
	private Integer id;
	/**
	 * @generated
	 */
	@PuiField(columnname = IVPuiDocument.MODEL_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 2, visibility = ColumnVisibility.visible)
	private String model;
	/**
	 * @generated
	 */
	@PuiField(columnname = IVPuiDocument.PK_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 3, visibility = ColumnVisibility.visible)
	private String pk;
	/**
	 * @generated
	 */
	@PuiField(columnname = IVPuiDocument.DESCRIPTION_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 4, visibility = ColumnVisibility.visible)
	private String description;
	/**
	 * @generated
	 */
	@PuiField(columnname = IVPuiDocument.LANGUAGE_COLUMN, ispk = false, nullable = true, type = ColumnType.text, autoincrementable = false, maxlength = 2, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 5, visibility = ColumnVisibility.visible)
	private String language;
	/**
	 * @generated
	 */
	@PuiField(columnname = IVPuiDocument.FILENAME_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 6, visibility = ColumnVisibility.visible)
	private String filename;
	/**
	 * @generated
	 */
	@PuiField(columnname = IVPuiDocument.FILENAME_ORIG_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 7, visibility = ColumnVisibility.visible)
	private String filenameorig;
	/**
	 * @generated
	 */
	@PuiField(columnname = IVPuiDocument.ROLE_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 8, visibility = ColumnVisibility.visible)
	private String role;
	/**
	 * @generated
	 */
	@PuiField(columnname = IVPuiDocument.ROLE_DESCRIPTION_COLUMN, ispk = false, nullable = true, type = ColumnType.text, autoincrementable = false, maxlength = 200, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 9, visibility = ColumnVisibility.visible)
	private String roledescription;
	/**
	 * @generated
	 */
	@PuiField(columnname = IVPuiDocument.THUMBNAILS_COLUMN, ispk = false, nullable = true, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 10, visibility = ColumnVisibility.visible)
	private String thumbnails;
	/**
	 * @generated
	 */
	@PuiField(columnname = IVPuiDocument.URL_COLUMN, ispk = false, nullable = true, type = ColumnType.text, autoincrementable = false, maxlength = 4000, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 11, visibility = ColumnVisibility.visible)
	private String url;
	/**
	 * @generated
	 */
	@PuiField(columnname = IVPuiDocument.DATETIME_COLUMN, ispk = false, nullable = false, type = ColumnType.datetime, autoincrementable = false, maxlength = -1, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 12, visibility = ColumnVisibility.visible)
	private java.time.Instant datetime;
	/**
	 * @generated
	 */
	@PuiField(columnname = IVPuiDocument.LANG_COLUMN, ispk = false, nullable = true, type = ColumnType.text, autoincrementable = false, maxlength = 2, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 13, visibility = ColumnVisibility.completelyhidden)
	private String lang;

	/**
	 * @generated
	 */
	@Override
	public Integer getId() {
		return id;
	}

	/**
	 * @generated
	 */
	@Override
	public void setId(Integer id) {
		this.id = id;
	}

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
	public String getRoledescription() {
		return roledescription;
	}

	/**
	 * @generated
	 */
	@Override
	public void setRoledescription(String roledescription) {
		this.roledescription = roledescription;
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
	public String getUrl() {
		return url;
	}

	/**
	 * @generated
	 */
	@Override
	public void setUrl(String url) {
		this.url = url;
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
	public String getLang() {
		return lang;
	}

	/**
	 * @generated
	 */
	@Override
	public void setLang(String lang) {
		this.lang = lang;
	}
}

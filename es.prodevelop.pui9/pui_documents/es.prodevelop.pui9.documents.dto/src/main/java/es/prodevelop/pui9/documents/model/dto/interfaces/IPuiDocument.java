package es.prodevelop.pui9.documents.model.dto.interfaces;

/**
 * @generated
 */
public interface IPuiDocument extends IPuiDocumentPk {
	/**
	 * @generated
	 */
	String MODEL_COLUMN = "model";
	/**
	 * @generated
	 */
	String MODEL_FIELD = "model";
	/**
	 * @generated
	 */
	String PK_COLUMN = "pk";
	/**
	 * @generated
	 */
	String PK_FIELD = "pk";
	/**
	 * @generated
	 */
	String LANGUAGE_COLUMN = "language";
	/**
	 * @generated
	 */
	String LANGUAGE_FIELD = "language";
	/**
	 * @generated
	 */
	String DESCRIPTION_COLUMN = "description";
	/**
	 * @generated
	 */
	String DESCRIPTION_FIELD = "description";
	/**
	 * @generated
	 */
	String FILENAME_COLUMN = "filename";
	/**
	 * @generated
	 */
	String FILENAME_FIELD = "filename";
	/**
	 * @generated
	 */
	String FILENAME_ORIG_COLUMN = "filename_orig";
	/**
	 * @generated
	 */
	String FILENAME_ORIG_FIELD = "filenameorig";
	/**
	 * @generated
	 */
	String ROLE_COLUMN = "role";
	/**
	 * @generated
	 */
	String ROLE_FIELD = "role";
	/**
	 * @generated
	 */
	String THUMBNAILS_COLUMN = "thumbnails";
	/**
	 * @generated
	 */
	String THUMBNAILS_FIELD = "thumbnails";
	/**
	 * @generated
	 */
	String DATETIME_COLUMN = "datetime";
	/**
	 * @generated
	 */
	String DATETIME_FIELD = "datetime";

	/**
	 * @generated
	 */
	String getModel();

	/**
	 * @generated
	 */
	void setModel(String model);

	/**
	 * @generated
	 */
	String getPk();

	/**
	 * @generated
	 */
	void setPk(String pk);

	/**
	 * @generated
	 */
	String getLanguage();

	/**
	 * @generated
	 */
	void setLanguage(String language);

	/**
	 * @generated
	 */
	String getDescription();

	/**
	 * @generated
	 */
	void setDescription(String description);

	/**
	 * @generated
	 */
	String getFilename();

	/**
	 * @generated
	 */
	void setFilename(String filename);

	/**
	 * @generated
	 */
	String getFilenameorig();

	/**
	 * @generated
	 */
	void setFilenameorig(String filenameorig);

	/**
	 * @generated
	 */
	String getRole();

	/**
	 * @generated
	 */
	void setRole(String role);

	/**
	 * @generated
	 */
	String getThumbnails();

	/**
	 * @generated
	 */
	void setThumbnails(String thumbnails);

	/**
	 * @generated
	 */
	java.time.Instant getDatetime();

	/**
	 * @generated
	 */
	void setDatetime(java.time.Instant datetime);

	String getUrl();

	void setUrl(String url);
}

package es.prodevelop.pui9.common.model.dto.interfaces;

/**
 * @generated
 */
public interface IPuiImportexport extends IPuiImportexportPk {
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
	String USR_COLUMN = "usr";
	/**
	 * @generated
	 */
	String USR_FIELD = "usr";
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
	String FILENAME_CSV_COLUMN = "filename_csv";
	/**
	 * @generated
	 */
	String FILENAME_CSV_FIELD = "filenamecsv";
	/**
	 * @generated
	 */
	String FILENAME_JSON_COLUMN = "filename_json";
	/**
	 * @generated
	 */
	String FILENAME_JSON_FIELD = "filenamejson";
	/**
	 * @generated
	 */
	String EXECUTED_COLUMN = "executed";
	/**
	 * @generated
	 */
	String EXECUTED_FIELD = "executed";

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
	String getUsr();

	/**
	 * @generated
	 */
	void setUsr(String usr);

	/**
	 * @generated
	 */
	java.time.Instant getDatetime();

	/**
	 * @generated
	 */
	void setDatetime(java.time.Instant datetime);

	/**
	 * @generated
	 */
	String getFilenamecsv();

	/**
	 * @generated
	 */
	void setFilenamecsv(String filenamecsv);

	/**
	 * @generated
	 */
	String getFilenamejson();

	/**
	 * @generated
	 */
	void setFilenamejson(String filenamejson);

	/**
	 * @generated
	 */
	Integer getExecuted();

	/**
	 * @generated
	 */
	void setExecuted(Integer executed);
}

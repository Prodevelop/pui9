package es.prodevelop.pui9.publishaudit.model.dto.interfaces;

/**
 * @generated
 */
public interface IPuiPublishAuditEntity extends IPuiPublishAuditEntityPk {
	/**
	 * @generated
	 */
	String ENTITY_COLUMN = "entity";
	/**
	 * @generated
	 */
	String ENTITY_FIELD = "entity";
	/**
	 * @generated
	 */
	String AUDIT_INSERT_COLUMN = "audit_insert";
	/**
	 * @generated
	 */
	String AUDIT_INSERT_FIELD = "auditinsert";
	/**
	 * @generated
	 */
	String AUDIT_UPDATE_COLUMN = "audit_update";
	/**
	 * @generated
	 */
	String AUDIT_UPDATE_FIELD = "auditupdate";
	/**
	 * @generated
	 */
	String AUDIT_DELETE_COLUMN = "audit_delete";
	/**
	 * @generated
	 */
	String AUDIT_DELETE_FIELD = "auditdelete";

	/**
	 * @generated
	 */
	String getEntity();

	/**
	 * @generated
	 */
	void setEntity(String entity);

	/**
	 * @generated
	 */
	Integer getAuditinsert();

	/**
	 * @generated
	 */
	void setAuditinsert(Integer auditinsert);

	/**
	 * @generated
	 */
	Integer getAuditupdate();

	/**
	 * @generated
	 */
	void setAuditupdate(Integer auditupdate);

	/**
	 * @generated
	 */
	Integer getAuditdelete();

	/**
	 * @generated
	 */
	void setAuditdelete(Integer auditdelete);
}

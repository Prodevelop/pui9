package es.prodevelop.pui9.publishaudit.model.dto.interfaces;

/**
 * @generated
 */
public interface IPuiPublishAuditTopic extends IPuiPublishAuditTopicPk {
	/**
	 * @generated
	 */
	String ID_ENTITY_COLUMN = "id_entity";
	/**
	 * @generated
	 */
	String ID_ENTITY_FIELD = "identity";
	/**
	 * @generated
	 */
	String OP_TYPE_COLUMN = "op_type";
	/**
	 * @generated
	 */
	String OP_TYPE_FIELD = "optype";
	/**
	 * @generated
	 */
	String COD_TOPIC_COLUMN = "cod_topic";
	/**
	 * @generated
	 */
	String COD_TOPIC_FIELD = "codtopic";
	/**
	 * @generated
	 */
	String ENABLED_COLUMN = "enabled";
	/**
	 * @generated
	 */
	String ENABLED_FIELD = "enabled";
	/**
	 * @generated
	 */
	String EVENT_TYPE_COLUMN = "event_type";
	/**
	 * @generated
	 */
	String EVENT_TYPE_FIELD = "eventtype";

	/**
	 * @generated
	 */
	Integer getIdentity();

	/**
	 * @generated
	 */
	void setIdentity(Integer identity);

	/**
	 * @generated
	 */
	String getOptype();

	/**
	 * @generated
	 */
	void setOptype(String optype);

	/**
	 * @generated
	 */
	String getCodtopic();

	/**
	 * @generated
	 */
	void setCodtopic(String codtopic);

	/**
	 * @generated
	 */
	Integer getEnabled();

	/**
	 * @generated
	 */
	void setEnabled(Integer enabled);

	/**
	 * @generated
	 */
	Integer getEventtype();

	/**
	 * @generated
	 */
	void setEventtype(Integer eventtype);
}

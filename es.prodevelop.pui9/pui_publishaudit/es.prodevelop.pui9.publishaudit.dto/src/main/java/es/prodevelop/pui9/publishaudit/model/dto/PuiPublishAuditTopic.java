package es.prodevelop.pui9.publishaudit.model.dto;

import es.prodevelop.pui9.annotations.PuiEntity;
import es.prodevelop.pui9.annotations.PuiField;
import es.prodevelop.pui9.enums.ColumnType;
import es.prodevelop.pui9.publishaudit.model.dto.interfaces.IPuiPublishAuditTopic;

/**
 * @generated
 */
@PuiEntity(tablename = "pui_publish_audit_topic")
public class PuiPublishAuditTopic extends PuiPublishAuditTopicPk implements IPuiPublishAuditTopic {
	/**
	 * @generated
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * @generated
	 */
	@PuiField(columnname = IPuiPublishAuditTopic.ID_ENTITY_COLUMN, ispk = false, nullable = false, type = ColumnType.numeric, autoincrementable = false, maxlength = -1, islang = false, isgeometry = false, issequence = false)
	private Integer identity;
	/**
	 * @generated
	 */
	@PuiField(columnname = IPuiPublishAuditTopic.OP_TYPE_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 10, islang = false, isgeometry = false, issequence = false)
	private String optype;
	/**
	 * @generated
	 */
	@PuiField(columnname = IPuiPublishAuditTopic.COD_TOPIC_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	private String codtopic;
	/**
	 * @generated
	 */
	@PuiField(columnname = IPuiPublishAuditTopic.ENABLED_COLUMN, ispk = false, nullable = false, type = ColumnType.numeric, autoincrementable = false, maxlength = -1, islang = false, isgeometry = false, issequence = false)
	private Integer enabled = 1;
	/**
	 * @generated
	 */
	@PuiField(columnname = IPuiPublishAuditTopic.EVENT_TYPE_COLUMN, ispk = false, nullable = false, type = ColumnType.numeric, autoincrementable = false, maxlength = -1, islang = false, isgeometry = false, issequence = false)
	private Integer eventtype;

	/**
	 * @generated
	 */
	@Override
	public Integer getIdentity() {
		return identity;
	}

	/**
	 * @generated
	 */
	@Override
	public void setIdentity(Integer identity) {
		this.identity = identity;
	}

	/**
	 * @generated
	 */
	@Override
	public String getOptype() {
		return optype;
	}

	/**
	 * @generated
	 */
	@Override
	public void setOptype(String optype) {
		this.optype = optype;
	}

	/**
	 * @generated
	 */
	@Override
	public String getCodtopic() {
		return codtopic;
	}

	/**
	 * @generated
	 */
	@Override
	public void setCodtopic(String codtopic) {
		this.codtopic = codtopic;
	}

	/**
	 * @generated
	 */
	@Override
	public Integer getEnabled() {
		return enabled;
	}

	/**
	 * @generated
	 */
	@Override
	public void setEnabled(Integer enabled) {
		this.enabled = enabled;
	}

	/**
	 * @generated
	 */
	@Override
	public Integer getEventtype() {
		return eventtype;
	}

	/**
	 * @generated
	 */
	@Override
	public void setEventtype(Integer eventtype) {
		this.eventtype = eventtype;
	}
}

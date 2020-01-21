package es.prodevelop.pui9.publishaudit.model.dto;

import es.prodevelop.pui9.annotations.PuiField;
import es.prodevelop.pui9.enums.ColumnType;
import es.prodevelop.pui9.model.dto.AbstractTableDto;
import es.prodevelop.pui9.publishaudit.model.dto.interfaces.IPuiPublishAuditTopicPk;
import es.prodevelop.pui9.utils.PuiObjectUtils;

/**
 * @generated
 */
public class PuiPublishAuditTopicPk extends AbstractTableDto implements IPuiPublishAuditTopicPk {
	/**
	 * @generated
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * @generated
	 */
	@PuiField(columnname = IPuiPublishAuditTopicPk.ID_COLUMN, ispk = true, nullable = false, type = ColumnType.numeric, autoincrementable = true, maxlength = -1, islang = false, isgeometry = false, issequence = false)
	private Integer id;

	/**
	 * @generated
	 */
	public PuiPublishAuditTopicPk() {
	}

	/**
	 * @generated
	 */
	public PuiPublishAuditTopicPk(Integer id) {
		this.id = id;
	}

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
	@SuppressWarnings("unchecked")
	public PuiPublishAuditTopicPk createPk() {
		PuiPublishAuditTopicPk pk = new PuiPublishAuditTopicPk();
		PuiObjectUtils.copyProperties(pk, this);
		return pk;
	}
}

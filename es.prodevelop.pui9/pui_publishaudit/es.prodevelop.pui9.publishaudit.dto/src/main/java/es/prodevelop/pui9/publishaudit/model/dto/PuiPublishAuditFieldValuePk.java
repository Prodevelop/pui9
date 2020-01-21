package es.prodevelop.pui9.publishaudit.model.dto;

import es.prodevelop.pui9.annotations.PuiField;
import es.prodevelop.pui9.enums.ColumnType;
import es.prodevelop.pui9.model.dto.AbstractTableDto;
import es.prodevelop.pui9.publishaudit.model.dto.interfaces.IPuiPublishAuditFieldValuePk;
import es.prodevelop.pui9.utils.PuiObjectUtils;

/**
 * @generated
 */
public class PuiPublishAuditFieldValuePk extends AbstractTableDto implements IPuiPublishAuditFieldValuePk {
	/**
	 * @generated
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * @generated
	 */
	@PuiField(columnname = IPuiPublishAuditFieldValuePk.ID_COLUMN, ispk = true, nullable = false, type = ColumnType.numeric, autoincrementable = true, maxlength = -1, islang = false, isgeometry = false, issequence = false)
	private Integer id;

	/**
	 * @generated
	 */
	public PuiPublishAuditFieldValuePk() {
	}

	/**
	 * @generated
	 */
	public PuiPublishAuditFieldValuePk(Integer id) {
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
	public PuiPublishAuditFieldValuePk createPk() {
		PuiPublishAuditFieldValuePk pk = new PuiPublishAuditFieldValuePk();
		PuiObjectUtils.copyProperties(pk, this);
		return pk;
	}
}

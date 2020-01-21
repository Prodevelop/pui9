package es.prodevelop.pui9.docgen.model.dto;

import es.prodevelop.pui9.annotations.PuiField;
import es.prodevelop.pui9.docgen.model.dto.interfaces.IPuiDocgenTemplatePk;
import es.prodevelop.pui9.enums.ColumnType;
import es.prodevelop.pui9.model.dto.AbstractTableDto;
import es.prodevelop.pui9.utils.PuiObjectUtils;

/**
 * @generated
 */
public class PuiDocgenTemplatePk extends AbstractTableDto implements IPuiDocgenTemplatePk {
	/**
	 * @generated
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * @generated
	 */
	@PuiField(columnname = IPuiDocgenTemplatePk.ID_COLUMN, ispk = true, nullable = false, type = ColumnType.numeric, autoincrementable = true, maxlength = -1, islang = false, isgeometry = false, issequence = false)
	private Integer id;

	/**
	 * @generated
	 */
	public PuiDocgenTemplatePk() {
	}

	/**
	 * @generated
	 */
	public PuiDocgenTemplatePk(Integer id) {
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
	public PuiDocgenTemplatePk createPk() {
		PuiDocgenTemplatePk pk = new PuiDocgenTemplatePk();
		PuiObjectUtils.copyProperties(pk, this);
		return pk;
	}
}

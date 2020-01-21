package es.prodevelop.pui9.common.model.dto;

import es.prodevelop.pui9.annotations.PuiField;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiImportexportPk;
import es.prodevelop.pui9.enums.ColumnType;
import es.prodevelop.pui9.model.dto.AbstractTableDto;
import es.prodevelop.pui9.utils.PuiObjectUtils;

/**
 * @generated
 */
public class PuiImportexportPk extends AbstractTableDto implements IPuiImportexportPk {
	/**
	 * @generated
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * @generated
	 */
	@PuiField(columnname = IPuiImportexportPk.ID_COLUMN, ispk = true, nullable = false, type = ColumnType.numeric, autoincrementable = true, maxlength = -1, islang = false, isgeometry = false, issequence = false)
	private Integer id;

	/**
	 * @generated
	 */
	public PuiImportexportPk() {
	}

	/**
	 * @generated
	 */
	public PuiImportexportPk(Integer id) {
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
	public PuiImportexportPk createPk() {
		PuiImportexportPk pk = new PuiImportexportPk();
		PuiObjectUtils.copyProperties(pk, this);
		return pk;
	}
}

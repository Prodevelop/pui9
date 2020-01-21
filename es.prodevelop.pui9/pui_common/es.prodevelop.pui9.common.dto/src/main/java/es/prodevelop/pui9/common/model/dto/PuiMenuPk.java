package es.prodevelop.pui9.common.model.dto;

import es.prodevelop.pui9.annotations.PuiField;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiMenuPk;
import es.prodevelop.pui9.enums.ColumnType;
import es.prodevelop.pui9.model.dto.AbstractTableDto;
import es.prodevelop.pui9.utils.PuiObjectUtils;

/**
 * @generated
 */
public class PuiMenuPk extends AbstractTableDto implements IPuiMenuPk {
	/**
	 * @generated
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * @generated
	 */
	@PuiField(columnname = IPuiMenuPk.NODE_COLUMN, ispk = true, nullable = false, type = ColumnType.numeric, autoincrementable = false, maxlength = -1, islang = false, isgeometry = false, issequence = false)
	private Integer node;

	/**
	 * @generated
	 */
	public PuiMenuPk() {
	}

	/**
	 * @generated
	 */
	public PuiMenuPk(Integer node) {
		this.node = node;
	}

	/**
	 * @generated
	 */
	@Override
	public Integer getNode() {
		return node;
	}

	/**
	 * @generated
	 */
	@Override
	public void setNode(Integer node) {
		this.node = node;
	}

	/**
	 * @generated
	 */
	@Override
	@SuppressWarnings("unchecked")
	public PuiMenuPk createPk() {
		PuiMenuPk pk = new PuiMenuPk();
		PuiObjectUtils.copyProperties(pk, this);
		return pk;
	}
}

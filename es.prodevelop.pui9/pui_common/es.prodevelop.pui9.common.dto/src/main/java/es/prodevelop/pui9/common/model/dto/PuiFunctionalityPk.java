package es.prodevelop.pui9.common.model.dto;

import es.prodevelop.pui9.annotations.PuiField;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiFunctionalityPk;
import es.prodevelop.pui9.enums.ColumnType;
import es.prodevelop.pui9.model.dto.AbstractTableDto;
import es.prodevelop.pui9.utils.PuiObjectUtils;

/**
 * @generated
 */
public class PuiFunctionalityPk extends AbstractTableDto implements IPuiFunctionalityPk {
	/**
	 * @generated
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * @generated
	 */
	@PuiField(columnname = IPuiFunctionalityPk.FUNCTIONALITY_COLUMN, ispk = true, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	private String functionality;

	/**
	 * @generated
	 */
	public PuiFunctionalityPk() {
	}

	/**
	 * @generated
	 */
	public PuiFunctionalityPk(String functionality) {
		this.functionality = functionality;
	}

	/**
	 * @generated
	 */
	@Override
	public String getFunctionality() {
		return functionality;
	}

	/**
	 * @generated
	 */
	@Override
	public void setFunctionality(String functionality) {
		this.functionality = functionality;
	}

	/**
	 * @generated
	 */
	@Override
	@SuppressWarnings("unchecked")
	public PuiFunctionalityPk createPk() {
		PuiFunctionalityPk pk = new PuiFunctionalityPk();
		PuiObjectUtils.copyProperties(pk, this);
		return pk;
	}
}

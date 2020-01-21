package es.prodevelop.pui9.documents.model.dto;

import es.prodevelop.pui9.annotations.PuiField;
import es.prodevelop.pui9.documents.model.dto.interfaces.IPuiDocumentPk;
import es.prodevelop.pui9.enums.ColumnType;
import es.prodevelop.pui9.model.dto.AbstractTableDto;
import es.prodevelop.pui9.utils.PuiObjectUtils;

/**
 * @generated
 */
public class PuiDocumentPk extends AbstractTableDto implements IPuiDocumentPk {
	/**
	 * @generated
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * @generated
	 */
	@PuiField(columnname = IPuiDocumentPk.ID_COLUMN, ispk = true, nullable = false, type = ColumnType.numeric, autoincrementable = true, maxlength = -1, islang = false, isgeometry = false, issequence = false)
	private Integer id;

	/**
	 * @generated
	 */
	public PuiDocumentPk() {
	}

	/**
	 * @generated
	 */
	public PuiDocumentPk(Integer id) {
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
	public PuiDocumentPk createPk() {
		PuiDocumentPk pk = new PuiDocumentPk();
		PuiObjectUtils.copyProperties(pk, this);
		return pk;
	}
}

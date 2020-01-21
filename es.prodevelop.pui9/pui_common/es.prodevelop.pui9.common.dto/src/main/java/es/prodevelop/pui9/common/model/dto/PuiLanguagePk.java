package es.prodevelop.pui9.common.model.dto;

import es.prodevelop.pui9.annotations.PuiField;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiLanguagePk;
import es.prodevelop.pui9.enums.ColumnType;
import es.prodevelop.pui9.model.dto.AbstractTableDto;
import es.prodevelop.pui9.utils.PuiObjectUtils;

/**
 * @generated
 */
public class PuiLanguagePk extends AbstractTableDto implements IPuiLanguagePk {
	/**
	 * @generated
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * @generated
	 */
	@PuiField(columnname = IPuiLanguagePk.ISOCODE_COLUMN, ispk = true, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 2, islang = false, isgeometry = false, issequence = false)
	private String isocode;

	/**
	 * @generated
	 */
	public PuiLanguagePk() {
	}

	/**
	 * @generated
	 */
	public PuiLanguagePk(String isocode) {
		this.isocode = isocode;
	}

	/**
	 * @generated
	 */
	@Override
	public String getIsocode() {
		return isocode;
	}

	/**
	 * @generated
	 */
	@Override
	public void setIsocode(String isocode) {
		this.isocode = isocode;
	}

	/**
	 * @generated
	 */
	@Override
	@SuppressWarnings("unchecked")
	public PuiLanguagePk createPk() {
		PuiLanguagePk pk = new PuiLanguagePk();
		PuiObjectUtils.copyProperties(pk, this);
		return pk;
	}
}

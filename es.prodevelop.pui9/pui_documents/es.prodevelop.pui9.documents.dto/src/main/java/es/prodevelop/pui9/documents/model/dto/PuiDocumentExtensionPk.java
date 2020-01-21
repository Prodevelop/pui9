package es.prodevelop.pui9.documents.model.dto;

import es.prodevelop.pui9.annotations.PuiField;
import es.prodevelop.pui9.documents.model.dto.interfaces.IPuiDocumentExtensionPk;
import es.prodevelop.pui9.enums.ColumnType;
import es.prodevelop.pui9.model.dto.AbstractTableDto;
import es.prodevelop.pui9.utils.PuiObjectUtils;

/**
 * @generated
 */
public class PuiDocumentExtensionPk extends AbstractTableDto implements IPuiDocumentExtensionPk {
	/**
	 * @generated
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * @generated
	 */
	@PuiField(columnname = IPuiDocumentExtensionPk.EXTENSION_COLUMN, ispk = true, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 10, islang = false, isgeometry = false, issequence = false)
	private String extension;

	/**
	 * @generated
	 */
	public PuiDocumentExtensionPk() {
	}

	/**
	 * @generated
	 */
	public PuiDocumentExtensionPk(String extension) {
		this.extension = extension;
	}

	/**
	 * @generated
	 */
	@Override
	public String getExtension() {
		return extension;
	}

	/**
	 * @generated
	 */
	@Override
	public void setExtension(String extension) {
		this.extension = extension;
	}

	/**
	 * @generated
	 */
	@Override
	@SuppressWarnings("unchecked")
	public PuiDocumentExtensionPk createPk() {
		PuiDocumentExtensionPk pk = new PuiDocumentExtensionPk();
		PuiObjectUtils.copyProperties(pk, this);
		return pk;
	}
}

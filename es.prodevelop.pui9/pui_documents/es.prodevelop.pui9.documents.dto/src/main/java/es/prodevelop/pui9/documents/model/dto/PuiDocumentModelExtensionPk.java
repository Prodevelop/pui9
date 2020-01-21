package es.prodevelop.pui9.documents.model.dto;

import es.prodevelop.pui9.annotations.PuiField;
import es.prodevelop.pui9.documents.model.dto.interfaces.IPuiDocumentModelExtensionPk;
import es.prodevelop.pui9.enums.ColumnType;
import es.prodevelop.pui9.model.dto.AbstractTableDto;
import es.prodevelop.pui9.utils.PuiObjectUtils;

/**
 * @generated
 */
public class PuiDocumentModelExtensionPk extends AbstractTableDto implements IPuiDocumentModelExtensionPk {
	/**
	 * @generated
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * @generated
	 */
	@PuiField(columnname = IPuiDocumentModelExtensionPk.MODEL_COLUMN, ispk = true, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	private String model;
	/**
	 * @generated
	 */
	@PuiField(columnname = IPuiDocumentModelExtensionPk.EXTENSION_COLUMN, ispk = true, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 10, islang = false, isgeometry = false, issequence = false)
	private String extension;

	/**
	 * @generated
	 */
	public PuiDocumentModelExtensionPk() {
	}

	/**
	 * @generated
	 */
	public PuiDocumentModelExtensionPk(String extension, String model) {
		this.extension = extension;
		this.model = model;
	}

	/**
	 * @generated
	 */
	@Override
	public String getModel() {
		return model;
	}

	/**
	 * @generated
	 */
	@Override
	public void setModel(String model) {
		this.model = model;
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
	public PuiDocumentModelExtensionPk createPk() {
		PuiDocumentModelExtensionPk pk = new PuiDocumentModelExtensionPk();
		PuiObjectUtils.copyProperties(pk, this);
		return pk;
	}
}

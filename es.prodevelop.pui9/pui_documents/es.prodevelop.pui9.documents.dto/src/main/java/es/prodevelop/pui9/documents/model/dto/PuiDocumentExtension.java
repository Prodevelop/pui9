package es.prodevelop.pui9.documents.model.dto;

import es.prodevelop.pui9.annotations.PuiEntity;
import es.prodevelop.pui9.annotations.PuiField;
import es.prodevelop.pui9.documents.model.dto.interfaces.IPuiDocumentExtension;
import es.prodevelop.pui9.enums.ColumnType;

/**
 * @generated
 */
@PuiEntity(tablename = "pui_document_extension")
public class PuiDocumentExtension extends PuiDocumentExtensionPk implements IPuiDocumentExtension {
	/**
	 * @generated
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * @generated
	 */
	@PuiField(columnname = IPuiDocumentExtension.MAX_SIZE_COLUMN, ispk = false, nullable = true, type = ColumnType.numeric, autoincrementable = false, maxlength = -1, islang = false, isgeometry = false, issequence = false)
	private Integer maxsize;

	/**
	 * @generated
	 */
	@Override
	public Integer getMaxsize() {
		return maxsize;
	}

	/**
	 * @generated
	 */
	@Override
	public void setMaxsize(Integer maxsize) {
		this.maxsize = maxsize;
	}
}

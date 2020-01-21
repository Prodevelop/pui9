package es.prodevelop.pui9.documents.model.dto;

import es.prodevelop.pui9.annotations.PuiEntity;
import es.prodevelop.pui9.annotations.PuiField;
import es.prodevelop.pui9.documents.model.dto.interfaces.IPuiDocumentRoleTra;
import es.prodevelop.pui9.enums.ColumnType;

/**
 * @generated
 */
@PuiEntity(tablename = "pui_document_role_tra")
public class PuiDocumentRoleTra extends PuiDocumentRoleTraPk implements IPuiDocumentRoleTra {
	/**
	 * @generated
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * @generated
	 */
	@PuiField(columnname = IPuiDocumentRoleTra.LANG_STATUS_COLUMN, ispk = false, nullable = false, type = ColumnType.numeric, autoincrementable = false, maxlength = -1, islang = false, isgeometry = false, issequence = false)
	private Integer langstatus = 0;
	/**
	 * @generated
	 */
	@PuiField(columnname = IPuiDocumentRoleTra.DESCRIPTION_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 200, islang = false, isgeometry = false, issequence = false)
	private String description;

	/**
	 * @generated
	 */
	@Override
	public Integer getLangstatus() {
		return langstatus;
	}

	/**
	 * @generated
	 */
	@Override
	public void setLangstatus(Integer langstatus) {
		this.langstatus = langstatus;
	}

	/**
	 * @generated
	 */
	@Override
	public String getDescription() {
		return description;
	}

	/**
	 * @generated
	 */
	@Override
	public void setDescription(String description) {
		this.description = description;
	}
}

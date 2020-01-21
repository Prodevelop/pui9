package es.prodevelop.pui9.docgen.model.views.dto;

import es.prodevelop.pui9.annotations.PuiEntity;
import es.prodevelop.pui9.annotations.PuiField;
import es.prodevelop.pui9.annotations.PuiViewColumn;
import es.prodevelop.pui9.docgen.model.views.dto.interfaces.IVPuiDocgenModel;
import es.prodevelop.pui9.enums.ColumnType;
import es.prodevelop.pui9.enums.ColumnVisibility;
import es.prodevelop.pui9.model.dto.AbstractViewDto;

/**
 * @generated
 */
@PuiEntity(tablename = "v_pui_docgen_model")
public class VPuiDocgenModel extends AbstractViewDto implements IVPuiDocgenModel {
	/**
	 * @generated
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * @generated
	 */
	@PuiField(columnname = IVPuiDocgenModel.MODEL_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 1, visibility = ColumnVisibility.visible)
	private String model;
	/**
	 * @generated
	 */
	@PuiField(columnname = IVPuiDocgenModel.ENTITY_COLUMN, ispk = false, nullable = true, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 2, visibility = ColumnVisibility.visible)
	private String entity;
	/**
	 * @generated
	 */
	@PuiField(columnname = IVPuiDocgenModel.LABEL_COLUMN, ispk = false, nullable = true, type = ColumnType.text, autoincrementable = false, maxlength = 203, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 3, visibility = ColumnVisibility.visible)
	private String label;

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
	public String getEntity() {
		return entity;
	}

	/**
	 * @generated
	 */
	@Override
	public void setEntity(String entity) {
		this.entity = entity;
	}

	/**
	 * @generated
	 */
	@Override
	public String getLabel() {
		return label;
	}

	/**
	 * @generated
	 */
	@Override
	public void setLabel(String label) {
		this.label = label;
	}
}

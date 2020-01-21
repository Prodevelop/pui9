package es.prodevelop.pui9.docgen.model.views.dto;

import es.prodevelop.pui9.annotations.PuiEntity;
import es.prodevelop.pui9.annotations.PuiField;
import es.prodevelop.pui9.annotations.PuiViewColumn;
import es.prodevelop.pui9.docgen.model.views.dto.interfaces.IVPuiDocgenTemplate;
import es.prodevelop.pui9.enums.ColumnType;
import es.prodevelop.pui9.enums.ColumnVisibility;
import es.prodevelop.pui9.model.dto.AbstractViewDto;

/**
 * @generated
 */
@PuiEntity(tablename = "v_pui_docgen_template")
public class VPuiDocgenTemplate extends AbstractViewDto implements IVPuiDocgenTemplate {
	/**
	 * @generated
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * @generated
	 */
	@PuiField(columnname = IVPuiDocgenTemplate.ID_COLUMN, ispk = false, nullable = false, type = ColumnType.numeric, autoincrementable = false, maxlength = -1, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 1, visibility = ColumnVisibility.visible)
	private Integer id;
	/**
	 * @generated
	 */
	@PuiField(columnname = IVPuiDocgenTemplate.NAME_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 2, visibility = ColumnVisibility.visible)
	private String name;
	/**
	 * @generated
	 */
	@PuiField(columnname = IVPuiDocgenTemplate.DESCRIPTION_COLUMN, ispk = false, nullable = true, type = ColumnType.text, autoincrementable = false, maxlength = 1000, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 3, visibility = ColumnVisibility.visible)
	private String description;
	/**
	 * @generated
	 */
	@PuiField(columnname = IVPuiDocgenTemplate.MAIN_MODEL_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 4, visibility = ColumnVisibility.visible)
	private String mainmodel;
	/**
	 * @generated
	 */
	@PuiField(columnname = IVPuiDocgenTemplate.MODELS_COLUMN, ispk = false, nullable = true, type = ColumnType.text, autoincrementable = false, maxlength = 500, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 5, visibility = ColumnVisibility.visible)
	private String models;
	/**
	 * @generated
	 */
	@PuiField(columnname = IVPuiDocgenTemplate.FILENAME_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 8, visibility = ColumnVisibility.visible)
	private String filename;
	/**
	 * @generated
	 */
	@PuiField(columnname = IVPuiDocgenTemplate.COLUMN_FILENAME_COLUMN, ispk = false, nullable = true, type = ColumnType.text, autoincrementable = false, maxlength = 200, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 6, visibility = ColumnVisibility.visible)
	private String columnfilename;
	/**
	 * @generated
	 */
	@PuiField(columnname = IVPuiDocgenTemplate.LABEL_COLUMN, ispk = false, nullable = true, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 7, visibility = ColumnVisibility.visible)
	private String label;

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
	public String getName() {
		return name;
	}

	/**
	 * @generated
	 */
	@Override
	public void setName(String name) {
		this.name = name;
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

	/**
	 * @generated
	 */
	@Override
	public String getMainmodel() {
		return mainmodel;
	}

	/**
	 * @generated
	 */
	@Override
	public void setMainmodel(String mainmodel) {
		this.mainmodel = mainmodel;
	}

	/**
	 * @generated
	 */
	@Override
	public String getModels() {
		return models;
	}

	/**
	 * @generated
	 */
	@Override
	public void setModels(String models) {
		this.models = models;
	}

	/**
	 * @generated
	 */
	@Override
	public String getFilename() {
		return filename;
	}

	/**
	 * @generated
	 */
	@Override
	public void setFilename(String filename) {
		this.filename = filename;
	}

	/**
	 * @generated
	 */
	@Override
	public String getColumnfilename() {
		return columnfilename;
	}

	/**
	 * @generated
	 */
	@Override
	public void setColumnfilename(String columnfilename) {
		this.columnfilename = columnfilename;
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

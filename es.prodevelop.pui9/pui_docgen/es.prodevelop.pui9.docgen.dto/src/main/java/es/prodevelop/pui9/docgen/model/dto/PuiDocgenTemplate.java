package es.prodevelop.pui9.docgen.model.dto;

import es.prodevelop.pui9.annotations.PuiEntity;
import es.prodevelop.pui9.annotations.PuiField;
import es.prodevelop.pui9.docgen.model.dto.interfaces.IPuiDocgenTemplate;
import es.prodevelop.pui9.enums.ColumnType;

/**
 * @generated
 */
@PuiEntity(tablename = "pui_docgen_template")
public class PuiDocgenTemplate extends PuiDocgenTemplatePk implements IPuiDocgenTemplate {
	/**
	 * @generated
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * @generated
	 */
	@PuiField(columnname = IPuiDocgenTemplate.NAME_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	private String name;
	/**
	 * @generated
	 */
	@PuiField(columnname = IPuiDocgenTemplate.DESCRIPTION_COLUMN, ispk = false, nullable = true, type = ColumnType.text, autoincrementable = false, maxlength = 1000, islang = false, isgeometry = false, issequence = false)
	private String description;
	/**
	 * @generated
	 */
	@PuiField(columnname = IPuiDocgenTemplate.MAIN_MODEL_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	private String mainmodel;
	/**
	 * @generated
	 */
	@PuiField(columnname = IPuiDocgenTemplate.MODELS_COLUMN, ispk = false, nullable = true, type = ColumnType.text, autoincrementable = false, maxlength = 500, islang = false, isgeometry = false, issequence = false)
	private String models;
	/**
	 * @generated
	 */
	@PuiField(columnname = IPuiDocgenTemplate.FILENAME_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	private String filename;
	/**
	 * @generated
	 */
	@PuiField(columnname = IPuiDocgenTemplate.MAPPING_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = -1, islang = false, isgeometry = false, issequence = false)
	private String mapping;
	/**
	 * @generated
	 */
	@PuiField(columnname = IPuiDocgenTemplate.FILTER_COLUMN, ispk = false, nullable = true, type = ColumnType.text, autoincrementable = false, maxlength = -1, islang = false, isgeometry = false, issequence = false)
	private String filter;
	/**
	 * @generated
	 */
	@PuiField(columnname = IPuiDocgenTemplate.PARAMETERS_COLUMN, ispk = false, nullable = true, type = ColumnType.text, autoincrementable = false, maxlength = -1, islang = false, isgeometry = false, issequence = false)
	private String parameters;
	/**
	 * @generated
	 */
	@PuiField(columnname = IPuiDocgenTemplate.COLUMN_FILENAME_COLUMN, ispk = false, nullable = true, type = ColumnType.text, autoincrementable = false, maxlength = 200, islang = false, isgeometry = false, issequence = false)
	private String columnfilename;

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
	public String getMapping() {
		return mapping;
	}

	/**
	 * @generated
	 */
	@Override
	public void setMapping(String mapping) {
		this.mapping = mapping;
	}

	/**
	 * @generated
	 */
	@Override
	public String getFilter() {
		return filter;
	}

	/**
	 * @generated
	 */
	@Override
	public void setFilter(String filter) {
		this.filter = filter;
	}

	/**
	 * @generated
	 */
	@Override
	public String getParameters() {
		return parameters;
	}

	/**
	 * @generated
	 */
	@Override
	public void setParameters(String parameters) {
		this.parameters = parameters;
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
}

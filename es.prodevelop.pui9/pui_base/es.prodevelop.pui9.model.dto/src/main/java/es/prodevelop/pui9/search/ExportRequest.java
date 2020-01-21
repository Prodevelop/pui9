package es.prodevelop.pui9.search;

import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Object to be used in the Grid Export request. Has the same attributes of a
 * {@link SearchRequest}, and others to specify the visible columns of the grid
 * and the exporting type
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
@ApiModel(value = "Export Request List description")
public class ExportRequest extends SearchRequest {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "A map representing the column name and the title of the column", required = false)
	private List<ExportColumnDefinition> exportColumns = new ArrayList<>();

	@ApiModelProperty(value = "The title for the export file", required = false)
	private String exportTitle;

	@ApiModelProperty(value = "The export type {csv, excel}", position = -1, required = false)
	private ExportType exportType = ExportType.excel;

	public List<ExportColumnDefinition> getExportColumns() {
		return exportColumns;
	}

	public void setExportColumns(List<ExportColumnDefinition> exportColumns) {
		this.exportColumns = exportColumns;
	}

	public String getExportTitle() {
		return exportTitle;
	}

	public void setExportTitle(String exportTitle) {
		this.exportTitle = exportTitle;
	}

	public ExportType getExportType() {
		return exportType != null ? exportType : ExportType.excel;
	}

	public void setExportType(ExportType exportType) {
		this.exportType = exportType;
	}

}

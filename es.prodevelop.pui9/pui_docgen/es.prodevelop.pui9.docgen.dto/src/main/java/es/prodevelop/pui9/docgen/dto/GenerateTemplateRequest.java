package es.prodevelop.pui9.docgen.dto;

import java.util.List;

import es.prodevelop.pui9.docgen.model.dto.interfaces.IPuiDocgenTemplatePk;
import es.prodevelop.pui9.filter.FilterRule;
import es.prodevelop.pui9.search.SearchRequest;
import io.swagger.annotations.ApiModelProperty;

public class GenerateTemplateRequest extends SearchRequest {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(required = true, example = "")
	private IPuiDocgenTemplatePk pk;
	@ApiModelProperty(required = false, example = "[]")
	private List<FilterRule> parameters;
	@ApiModelProperty(required = false, example = "[]")
	private List<MappingValueDto> mappings;
	@ApiModelProperty(required = true, example = "true")
	private boolean generatePdf;

	public IPuiDocgenTemplatePk getPk() {
		return pk;
	}

	public List<FilterRule> getParameters() {
		return parameters;
	}

	public List<MappingValueDto> getMappings() {
		return mappings;
	}

	public boolean isGeneratePdf() {
		return generatePdf;
	}

}

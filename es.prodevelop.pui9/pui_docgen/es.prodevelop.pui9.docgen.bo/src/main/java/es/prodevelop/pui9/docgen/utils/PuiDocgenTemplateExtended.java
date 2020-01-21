package es.prodevelop.pui9.docgen.utils;

import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import es.prodevelop.pui9.docgen.model.dto.PuiDocgenTemplate;
import es.prodevelop.pui9.docgen.model.dto.interfaces.IPuiDocgenTemplate;
import es.prodevelop.pui9.utils.PuiObjectUtils;
import io.swagger.annotations.ApiModelProperty;

public class PuiDocgenTemplateExtended extends PuiDocgenTemplate {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(hidden = true)
	private transient MultipartFile file;
	@ApiModelProperty(hidden = true)
	private transient String uniqueFilename;
	@ApiModelProperty(required = true)

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}

	@ApiModelProperty(hidden = true)
	public String getFullFileName() {
		return getFileName() + "." + getFileExtension();
	}

	@ApiModelProperty(hidden = true)
	public String getUniqueFullFileName() {
		if (uniqueFilename == null) {
			uniqueFilename = getFileName() + "_" + System.currentTimeMillis() + "." + getFileExtension();
		}
		return uniqueFilename;
	}

	private String getFileName() {
		return FilenameUtils.getBaseName(file.getOriginalFilename());
	}

	private String getFileExtension() {
		return FilenameUtils.getExtension(file.getOriginalFilename()).toLowerCase();
	}

	public IPuiDocgenTemplate asPuiDocgenTemplate() {
		IPuiDocgenTemplate template = new PuiDocgenTemplate();
		PuiObjectUtils.copyProperties(template, this);
		return template;
	}

}

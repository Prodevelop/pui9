package es.prodevelop.pui9.docgen.eventlistener.event;

import es.prodevelop.pui9.docgen.dto.GenerateTemplateRequest;
import es.prodevelop.pui9.docgen.model.dto.interfaces.IPuiDocgenTemplatePk;
import es.prodevelop.pui9.eventlistener.event.PuiEvent;
import es.prodevelop.pui9.file.FileDownload;

/**
 * Adapter for Delete an element from Database
 */
public class DocgenGenerateEvent extends PuiEvent<IPuiDocgenTemplatePk> {

	private static final long serialVersionUID = 1L;

	private GenerateTemplateRequest info;
	private FileDownload file;

	public DocgenGenerateEvent(GenerateTemplateRequest info, FileDownload file) {
		super(info.getPk(), "docgenGenerate");
		this.info = info;
		this.file = file;
	}

	public GenerateTemplateRequest getInfo() {
		return info;
	}

	public FileDownload getFile() {
		return file;
	}

}

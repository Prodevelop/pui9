package es.prodevelop.pui9.documents.service.interfaces;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import es.prodevelop.pui9.documents.exceptions.PuiDocumentsExtensionsException;
import es.prodevelop.pui9.documents.exceptions.PuiDocumentsFileSizeException;
import es.prodevelop.pui9.documents.exceptions.PuiDocumentsThumbnailException;
import es.prodevelop.pui9.documents.exceptions.PuiDocumentsUploadException;
import es.prodevelop.pui9.documents.model.dao.interfaces.IPuiDocumentDao;
import es.prodevelop.pui9.documents.model.dto.interfaces.IPuiDocument;
import es.prodevelop.pui9.documents.model.dto.interfaces.IPuiDocumentPk;
import es.prodevelop.pui9.documents.model.dto.interfaces.IPuiDocumentRolePk;
import es.prodevelop.pui9.documents.model.views.dao.interfaces.IVPuiDocumentDao;
import es.prodevelop.pui9.documents.model.views.dto.interfaces.IVPuiDocument;
import es.prodevelop.pui9.documents.utils.PuiDocumentLite;
import es.prodevelop.pui9.exceptions.PuiException;
import es.prodevelop.pui9.exceptions.PuiServiceInsertException;
import es.prodevelop.pui9.file.FileDownload;
import es.prodevelop.pui9.model.dto.interfaces.ITableDto;
import es.prodevelop.pui9.service.interfaces.IService;

/**
 * @generated
 */
public interface IPuiDocumentService
		extends IService<IPuiDocumentPk, IPuiDocument, IVPuiDocument, IPuiDocumentDao, IVPuiDocumentDao> {

	List<String> getRoles();

	List<String> getExtensionsForModel(String model);

	@Transactional(rollbackFor = PuiException.class)
	IPuiDocument upload(PuiDocumentLite document) throws PuiDocumentsUploadException, PuiServiceInsertException,
			PuiDocumentsThumbnailException, PuiDocumentsExtensionsException, PuiDocumentsFileSizeException;

	List<IPuiDocument> getDtoDocuments(ITableDto dto);

	List<IPuiDocument> getDtoDocuments(String model, String pk, IPuiDocumentRolePk role);

	IPuiDocument getDocumentFromFileName(String model, String pk, String filename);

	boolean existsDocumentFile(IPuiDocument dto);

	FileDownload getFile(IPuiDocumentPk pk);

	List<String> getModelsWithDocuments();

	void reloadDocumentsInfo();

	String getBaseDocumentsPath();

	void reloadThumbnails();
}

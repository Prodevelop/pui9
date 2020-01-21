package es.prodevelop.pui9.common.service.interfaces;

import es.prodevelop.pui9.common.model.dao.interfaces.IPuiImportexportDao;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiImportexport;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiImportexportPk;
import es.prodevelop.pui9.model.dao.interfaces.INullViewDao;
import es.prodevelop.pui9.model.dto.interfaces.INullView;
import es.prodevelop.pui9.service.interfaces.IService;

/**
 * @generated
 */
public interface IPuiImportexportService
		extends IService<IPuiImportexportPk, IPuiImportexport, INullView, IPuiImportexportDao, INullViewDao> {

	String getBaseDocumentsPath();

	String getImportFolder(String model, Integer id);

}

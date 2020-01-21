package es.prodevelop.pui9.common.service.interfaces;

import es.prodevelop.pui9.common.model.dao.interfaces.IPuiLanguageDao;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiLanguage;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiLanguagePk;
import es.prodevelop.pui9.common.model.views.dao.interfaces.IVPuiLanguageDao;
import es.prodevelop.pui9.common.model.views.dto.interfaces.IVPuiLanguage;
import es.prodevelop.pui9.service.interfaces.IService;

/**
 * @generated
 */
public interface IPuiLanguageService
		extends IService<IPuiLanguagePk, IPuiLanguage, IVPuiLanguage, IPuiLanguageDao, IVPuiLanguageDao> {

	IPuiLanguagePk getDefaultLanguage();

}
package es.prodevelop.pui9.common.service.interfaces;

import java.util.List;

import es.prodevelop.pui9.common.model.dao.interfaces.IPuiProfileDao;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiProfile;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiProfilePk;
import es.prodevelop.pui9.common.model.views.dao.interfaces.IVPuiProfileDao;
import es.prodevelop.pui9.common.model.views.dto.interfaces.IVPuiProfile;
import es.prodevelop.pui9.service.interfaces.IService;

/**
 * @generated
 */
public interface IPuiProfileService
		extends IService<IPuiProfilePk, IPuiProfile, IVPuiProfile, IPuiProfileDao, IVPuiProfileDao> {

	List<String> getFunctionalitiesForProfile(String profile);

	List<String> getFunctionalitiesForProfiles(List<String> profiles);

}
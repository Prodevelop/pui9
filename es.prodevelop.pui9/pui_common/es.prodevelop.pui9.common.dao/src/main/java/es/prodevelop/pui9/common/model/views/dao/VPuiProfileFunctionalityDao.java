package es.prodevelop.pui9.common.model.views.dao;

import org.springframework.stereotype.Repository;

import es.prodevelop.pui9.common.model.views.dao.interfaces.IVPuiProfileFunctionalityDao;
import es.prodevelop.pui9.common.model.views.dto.interfaces.IVPuiProfileFunctionality;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.AbstractViewDao;

/**
 * @generated
 */
@Repository
public class VPuiProfileFunctionalityDao extends AbstractViewDao<IVPuiProfileFunctionality>
		implements IVPuiProfileFunctionalityDao {
	/**
	 * @generated
	 */
	@Override
	public java.util.List<IVPuiProfileFunctionality> findByProfile(String profile) throws PuiDaoFindException {
		return super.findByColumn(IVPuiProfileFunctionality.PROFILE_FIELD, profile);
	}

	/**
	 * @generated
	 */
	@Override
	public java.util.List<IVPuiProfileFunctionality> findByProfilename(String profilename) throws PuiDaoFindException {
		return super.findByColumn(IVPuiProfileFunctionality.PROFILE_NAME_FIELD, profilename);
	}

	/**
	 * @generated
	 */
	@Override
	public java.util.List<IVPuiProfileFunctionality> findByFunctionality(String functionality)
			throws PuiDaoFindException {
		return super.findByColumn(IVPuiProfileFunctionality.FUNCTIONALITY_FIELD, functionality);
	}

	/**
	 * @generated
	 */
	@Override
	public java.util.List<IVPuiProfileFunctionality> findByLang(String lang) throws PuiDaoFindException {
		return super.findByColumn(IVPuiProfileFunctionality.LANG_FIELD, lang);
	}
}

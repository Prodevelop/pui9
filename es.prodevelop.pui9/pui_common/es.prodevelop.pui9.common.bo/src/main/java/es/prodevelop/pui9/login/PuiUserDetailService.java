package es.prodevelop.pui9.login;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import es.prodevelop.pui9.common.model.dao.interfaces.IPuiUserProfileDao;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiUser;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiUserProfile;
import es.prodevelop.pui9.common.model.views.dao.interfaces.IVPuiUserFunctionalityDao;
import es.prodevelop.pui9.common.model.views.dto.interfaces.IVPuiUserFunctionality;
import es.prodevelop.pui9.common.service.interfaces.IPuiUserService;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.login.PuiUserSession.PuiUserSessionFunctionality;
import es.prodevelop.pui9.login.PuiUserSession.PuiUserSessionProfile;
import es.prodevelop.pui9.utils.PuiConstants;
import es.prodevelop.pui9.utils.PuiLanguage;

/**
 * This class is used to controll the login of the users within the application
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
@Component
public class PuiUserDetailService implements UserDetailsService {

	@Autowired
	private IPuiUserService puiUserService;

	@Autowired
	private IVPuiUserFunctionalityDao userFunctionalityDao;

	@Autowired
	private IPuiUserProfileDao userProfileDao;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		IPuiUser puiUser = puiUserService.getUserLite(username);
		if (puiUser == null) {
			throw new UsernameNotFoundException(username);
		}

		List<PuiUserSessionProfile> fullProfiles = getProfilesAsString(puiUser.getUsr());
		List<PuiUserSessionFunctionality> fullFunctionalities = getFunctionalitiesAsString(puiUser.getUsr());

		return buildUserDetails(puiUser, fullProfiles, fullFunctionalities);
	}

	/**
	 * Get the list of profiles assigned to the user
	 * 
	 * @param user The user
	 * @return The list of profiles of the user
	 */
	protected List<PuiUserSessionProfile> getProfilesAsString(String user) {
		List<IPuiUserProfile> profiles;
		try {
			profiles = userProfileDao.findByUsr(user);
		} catch (PuiDaoFindException e) {
			profiles = Collections.emptyList();
		}

		List<PuiUserSessionProfile> fullProfiles = new ArrayList<>();
		profiles.forEach(profile -> fullProfiles.add(PuiUserSessionProfile.createNewProfile(profile.getProfile())));

		return fullProfiles;
	}

	/**
	 * Get the list of functionalities assigned to the user
	 * 
	 * @param user The user
	 * @return The list of functionalities of the user
	 */
	protected List<PuiUserSessionFunctionality> getFunctionalitiesAsString(String user) {
		List<IVPuiUserFunctionality> functionalities;
		try {
			functionalities = userFunctionalityDao.findByUsr(user);
		} catch (PuiDaoFindException e) {
			functionalities = Collections.emptyList();
		}

		List<PuiUserSessionFunctionality> fullFunctionalities = new ArrayList<>();
		functionalities.forEach(functionality -> fullFunctionalities.add(PuiUserSessionFunctionality
				.createNewFunctionality(functionality.getProfile(), functionality.getFunctionality())));

		return fullFunctionalities;
	}

	/**
	 * Build the object {@link PuiUserSession} with the given information
	 * 
	 * @param puiUser         The user
	 * @param profiles        The profiles
	 * @param functionalities The functionalities
	 * @return The object PuiUserSession
	 */
	protected PuiUserSession buildUserDetails(IPuiUser puiUser, List<PuiUserSessionProfile> profiles,
			List<PuiUserSessionFunctionality> functionalities) {
		return new PuiUserSession(puiUser.getUsr(), puiUser.getName(), puiUser.getPassword(),
				new PuiLanguage(puiUser.getLanguage()), puiUser.getEmail(), puiUser.getDateformat(),
				puiUser.getDisabled().equals(PuiConstants.TRUE_INT), false, false, false, puiUser.getLastaccesstime(),
				puiUser.getLastaccessip(), profiles, functionalities);
	}

}

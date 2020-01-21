package es.prodevelop.pui9.login;

import java.util.List;

import es.prodevelop.pui9.common.exceptions.PuiCommonAnonymousNotAllowedException;
import es.prodevelop.pui9.common.exceptions.PuiCommonIncorrectLoginException;
import es.prodevelop.pui9.common.exceptions.PuiCommonIncorrectUserPasswordException;
import es.prodevelop.pui9.common.exceptions.PuiCommonNoSessionException;
import es.prodevelop.pui9.common.exceptions.PuiCommonUserDisabledException;
import es.prodevelop.pui9.common.exceptions.PuiCommonUserNotExistsException;
import es.prodevelop.pui9.common.exceptions.PuiCommonUserSessionTimeoutException;

/**
 * 
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public interface IPuiLogin {

	/**
	 * Do the login of the given user into the application. The login is created by
	 * Spring Security
	 * 
	 * @param user      The user to be logged
	 * @param password  The password of the user
	 * @param ip        The IP of the client
	 * @param userAgent The User Agent of the client
	 * @return The JWT authorization token of the session
	 * @throws PuiCommonIncorrectUserPasswordException If the user or password are
	 *                                                 wrong
	 * @throws PuiCommonIncorrectLoginException        If an error while loging
	 *                                                 occurs
	 * @throws PuiCommonAnonymousNotAllowedException   If it's trying to start an
	 *                                                 anonymous session and the
	 *                                                 application doesn't allow it
	 * @throws PuiCommonUserNotExistsException         If the user doesn't exist
	 */
	PuiUserInfo loginUser(LoginData loginData)
			throws PuiCommonIncorrectUserPasswordException, PuiCommonIncorrectLoginException,
			PuiCommonAnonymousNotAllowedException, PuiCommonUserNotExistsException, PuiCommonUserDisabledException;

	/**
	 * Logout the user identified by the given jwt
	 * 
	 * @param authorization The authorization string (the JWT token)
	 * @throws PuiCommonNoSessionException If no authorization string is provided
	 */
	PuiUserSession logoutUser(String jwt) throws PuiCommonNoSessionException;

	/**
	 * Set the session for the current request
	 * 
	 * @param jwt                   The authorization string (the JWT token)
	 * @param launchNewSessionEvent Launches the newSession Event. It is launched
	 *                              always if set to true or if the session was
	 *                              recovered
	 * @return The authorization string updated (the expiration time is updated
	 * @throws PuiCommonNoSessionException          If no authorization string is
	 *                                              provided
	 * @throws PuiCommonUserSessionTimeoutException If the given authorization token
	 *                                              is expired
	 */
	void setSession(String jwt, boolean lauchNewSessionEvent)
			throws PuiCommonNoSessionException, PuiCommonUserSessionTimeoutException;

	/**
	 * Remove the session of the current logged user
	 */
	void removeSession();

	/**
	 * Get all the opened sessions
	 * 
	 * @return The list of all the opened sessions
	 */
	List<PuiUserSession> getAllSessions();

}

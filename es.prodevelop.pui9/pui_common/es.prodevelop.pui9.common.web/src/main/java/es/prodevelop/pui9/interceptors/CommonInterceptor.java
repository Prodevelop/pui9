package es.prodevelop.pui9.interceptors;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.pui9.PuiRequestMappingHandlerMapping;

import es.prodevelop.pui9.annotations.PuiFunctionality;
import es.prodevelop.pui9.common.exceptions.PuiCommonAnonymousNotAllowedException;
import es.prodevelop.pui9.common.exceptions.PuiCommonIncorrectLoginException;
import es.prodevelop.pui9.common.exceptions.PuiCommonIncorrectUserPasswordException;
import es.prodevelop.pui9.common.exceptions.PuiCommonNoSessionException;
import es.prodevelop.pui9.common.exceptions.PuiCommonNotAllowedException;
import es.prodevelop.pui9.common.exceptions.PuiCommonUserDisabledException;
import es.prodevelop.pui9.common.exceptions.PuiCommonUserNotExistsException;
import es.prodevelop.pui9.common.exceptions.PuiCommonUserSessionTimeoutException;
import es.prodevelop.pui9.components.PuiApplicationContext;
import es.prodevelop.pui9.enums.Pui9KnownClients;
import es.prodevelop.pui9.lang.LanguageThreadLocal;
import es.prodevelop.pui9.login.IPuiLogin;
import es.prodevelop.pui9.login.LoginData;
import es.prodevelop.pui9.login.PuiUserSession;
import es.prodevelop.pui9.model.dto.interfaces.IDto;
import es.prodevelop.pui9.utils.PuiLanguage;
import es.prodevelop.pui9.utils.PuiLanguageUtils;
import es.prodevelop.pui9.utils.PuiRequestUtils;

/**
 * This implementation of {@link BaseInterceptor}, adds support for the User
 * sessions and permissions to consume the web services.
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class CommonInterceptor extends PuiInterceptor {

	private static final String TOKEN_BASIC_PREFIX = "Basic ";
	public static final String HEADER_TIMEZONE = "Timezone";

	protected PuiRequestMappingHandlerMapping getPuiRequestMapping() {
		return PuiApplicationContext.getInstance().getBean(PuiRequestMappingHandlerMapping.class);
	}

	protected IPuiLogin getPuiLogin() {
		return PuiApplicationContext.getInstance().getBean(IPuiLogin.class);
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		LanguageThreadLocal.getSingleton().setData(request.getHeader(HttpHeaders.ACCEPT_LANGUAGE));

		if (getPuiRequestMapping().isSessionRequired(handler)) {
			if (isSwaggerRequest(request)) {
				initSwaggerRequest(request);
			} else {
				setSessionInfo(request);
			}
			checkUserPermission(handler);
			setLanguageToCurrentSession(request);
		} else {
			getPuiLogin().removeSession();
		}

		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		if (getPuiRequestMapping().isSessionRequired(handler) && isSwaggerRequest(request)) {
			finishSwaggerRequest();
		}
		getPuiLogin().removeSession();
	}

	/**
	 * Check if the given request is a Swagger request
	 * 
	 * @param request The request
	 * @return true if it's a Swagger request; false if not
	 */
	private boolean isSwaggerRequest(HttpServletRequest request) {
		String authorization = getRequestAuthorization(request);
		if (authorization == null || !authorization.startsWith(TOKEN_BASIC_PREFIX)) {
			return false;
		}

		authorization = authorization.replace(TOKEN_BASIC_PREFIX, "");
		String decoded = new String(Base64Utils.decodeFromString(authorization));
		String[] splits = decoded.split(":");

		return splits.length == 2;
	}

	/**
	 * Init the Swagger request
	 * 
	 * @param request The request
	 * @throws PuiCommonIncorrectLoginException        If an error when login occurs
	 * @throws PuiCommonIncorrectUserPasswordException If the user or password are
	 *                                                 wrong
	 * @throws PuiCommonAnonymousNotAllowedException   If it's trying to start an
	 *                                                 anonymous session and the
	 *                                                 application doesn't allow it
	 * @throws PuiCommonUserNotExistsException         If the user doesn't exist
	 */
	protected void initSwaggerRequest(HttpServletRequest request)
			throws PuiCommonIncorrectUserPasswordException, PuiCommonIncorrectLoginException,
			PuiCommonAnonymousNotAllowedException, PuiCommonUserNotExistsException, PuiCommonUserDisabledException {
		String authorization = getRequestAuthorization(request);
		authorization = authorization.replace(TOKEN_BASIC_PREFIX, "");
		String timezone = getRequestZoneOffset(request);

		String ip = PuiRequestUtils.extractIp(request);
		String userAgent = request.getHeader(HttpHeaders.USER_AGENT);

		String decoded = new String(Base64Utils.decodeFromString(authorization));
		String[] splits = decoded.split(":");
		if (splits.length != 2) {
			throw new PuiCommonIncorrectUserPasswordException();
		}

		String user = splits[0];
		String password = splits[1];

		LoginData loginData = new LoginData(user, password, false, ip, userAgent, timezone,
				Pui9KnownClients.SWAGGER_CLIENT.name());
		getPuiLogin().loginUser(loginData);
	}

	/**
	 * Finish the Swagger request
	 * 
	 * @param request The request
	 * @throws PuiCommonNoSessionException If no authorization string is provided
	 */
	protected void finishSwaggerRequest() throws PuiCommonNoSessionException {
		if (getUserSession() != null) {
			getPuiLogin().logoutUser(getUserSession().getJwt());
		}
	}

	/**
	 * Set the session information for this request. If this method is called, it
	 * means that a session should be initiated previously by the client. In that
	 * case, the session is cached, so it can be retrieved. If not exists, an error
	 * will be thrown, and the client should be initiate a new session
	 * 
	 * @param request The current request
	 * @throws PuiCommonNoSessionException          If no session exists previously
	 * @throws PuiCommonUserSessionTimeoutException If the existing session is
	 *                                              expired
	 */
	protected void setSessionInfo(HttpServletRequest request)
			throws PuiCommonNoSessionException, PuiCommonUserSessionTimeoutException {
		String authorization = getRequestAuthorization(request);
		getPuiLogin().setSession(authorization, false);
	}

	private String getRequestAuthorization(HttpServletRequest request) {
		return request.getHeader(HttpHeaders.AUTHORIZATION);
	}

	private String getRequestZoneOffset(HttpServletRequest request) {
		return request.getHeader(HEADER_TIMEZONE);
	}

	/**
	 * Check if the user has permission to execute the given Web Service. Any Web
	 * Service that requires permission, should declare the {@link PuiFunctionality}
	 * annotation. This annotation is used to extract the name of the funcionality
	 * that the user should have to consume it
	 * 
	 * @param handler The handler that represents the Web Service
	 * @throws PuiCommonNotAllowedException If the user has no permission to execute
	 *                                      it
	 */
	private void checkUserPermission(Object handler) throws PuiCommonNotAllowedException {
		String functionality = getPuiRequestMapping().getHandlerFunctionality(handler);
		boolean hasFunctionality = false;

		if (StringUtils.isEmpty(functionality)) {
			hasFunctionality = true;
		} else {
			hasFunctionality = PuiUserSession.getCurrentSession().getFunctionalities().stream()
					.filter(func -> func.equals(functionality)).count() > 0;
		}

		if (!hasFunctionality) {
			throw new PuiCommonNotAllowedException();
		}
	}

	/**
	 * If using a session, set the language for it. The order to get the language is
	 * the following:
	 * <p>
	 * <ol>
	 * <li><b>URL Parameter: </b>
	 * http://localhost:8080/appname/controller/action?<b>lang=en</b></li>
	 * <li><b>Browser language ('Accept-Language' header): </b>
	 * Accept-Language:en-US,en;q=0.8,en-GB;q=0.6,es;q=0.4,ca;q=0.2</li>
	 * <li><b>User default language: </b>The language choosen by the user in its
	 * profile configuration</li>
	 * <li><b>Default language in the BD: </b>The default language marked in the DB
	 * language table</li>
	 * </ol>
	 * 
	 * @param request The request servlet
	 */
	protected void setLanguageToCurrentSession(HttpServletRequest request) {
		// 1: URL parameter
		PuiLanguage lang = getLanguageFromUrlParameter(request);
		if (lang == null) {
			// 2: Request language
			lang = getLanguageFromLocale(request);
		}
		if (lang == null) {
			// 3: User language
			lang = getLanguageFromLoggedUser();
		}
		if (lang == null) {
			// 4: default in DB
			lang = getLanguageFromDB();
		}
		if (lang == null) {
			lang = PuiLanguage.DEFAULT_LANG;
		}

		PuiUserSession.getCurrentSession().setLanguage(lang);
	}

	/**
	 * Returns the language from the URL Parameters of the request. The parameter
	 * should be called 'lang'
	 * 
	 * @param request The current request
	 * @return The language if found or null
	 */
	protected PuiLanguage getLanguageFromUrlParameter(HttpServletRequest request) {
		String lang = request.getParameter(IDto.LANG_COLUMN_NAME);
		return PuiLanguageUtils.existLanguage(lang) ? new PuiLanguage(lang) : null;
	}

	/**
	 * Returns the language from the Locale of the request
	 * 
	 * @param request The current request
	 * @return The language if found or null
	 */
	protected PuiLanguage getLanguageFromLocale(HttpServletRequest request) {
		Locale locale = request.getLocale();
		return PuiLanguageUtils.existLanguage(locale) ? new PuiLanguage(locale) : null;
	}

	/**
	 * Returns the language that the logged user
	 * 
	 * @return The language if found or null
	 */
	protected PuiLanguage getLanguageFromLoggedUser() {
		PuiUserSession userSession = getUserSession();
		if (userSession != null) {
			return PuiLanguageUtils.existLanguage(userSession.getLanguage()) ? userSession.getLanguage() : null;
		} else {
			return null;
		}
	}

	protected PuiUserSession getUserSession() {
		return PuiUserSession.getCurrentSession();
	}

	/**
	 * Returns the default language in the DB
	 * 
	 * @return The language if found or null
	 */
	protected PuiLanguage getLanguageFromDB() {
		return PuiLanguageUtils.getDefaultLanguage();
	}

}

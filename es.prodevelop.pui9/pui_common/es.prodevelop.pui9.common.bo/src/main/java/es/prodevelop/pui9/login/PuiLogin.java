package es.prodevelop.pui9.login;

import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import es.prodevelop.pui9.common.enums.PuiVariableValues;
import es.prodevelop.pui9.common.exceptions.PuiCommonAnonymousNotAllowedException;
import es.prodevelop.pui9.common.exceptions.PuiCommonIncorrectLoginException;
import es.prodevelop.pui9.common.exceptions.PuiCommonIncorrectUserPasswordException;
import es.prodevelop.pui9.common.exceptions.PuiCommonNoSessionException;
import es.prodevelop.pui9.common.exceptions.PuiCommonUserDisabledException;
import es.prodevelop.pui9.common.exceptions.PuiCommonUserNotExistsException;
import es.prodevelop.pui9.common.exceptions.PuiCommonUserSessionTimeoutException;
import es.prodevelop.pui9.common.service.interfaces.IPuiVariableService;
import es.prodevelop.pui9.eventlistener.EventLauncher;
import es.prodevelop.pui9.eventlistener.event.SessionCreatedEvent;
import es.prodevelop.pui9.threads.PuiBackgroundExecutors;
import es.prodevelop.pui9.utils.PuiDateUtil;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClaims;
import io.jsonwebtoken.impl.DefaultJwsHeader;

/**
 * This component allows to manage the sessions in the application. It allows to
 * make the login/logout of a user and check the provided session. It uses the
 * standard JWT to generate the session tokens, and stores in it some necessary
 * information about the logged user
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
@Component
public class PuiLogin implements IPuiLogin {

	private static final String TOKEN_BEARER_PREFIX = "Bearer ";
	private static final String JWT_ISSUER = "PUI9_SERVER";
	private static final String JWT_CLAIM_PERSISTENT = "persistent";
	private static final String JWT_CLAIM_IP = "ip";
	private static final String JWT_CLAIM_TIMEZONE = "timezone";
	private static final String JWT_CLAIM_USER_AGENT = "useragent";
	private static final String JWT_CLAIM_CLIENT = "client";

	private static final Integer REFRESH_SESSIONS_TIMER_MINUTES = 30;

	@Autowired
	private IPuiVariableService variableService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private EventLauncher eventLauncher;

	private Map<String, SessionData> sessionsCache = new HashMap<>();
	private Map<String, SessionData> killedSessions = new HashMap<>();

	@PostConstruct
	private void postConstruct() {
		PuiBackgroundExecutors.getSingleton().registerNewExecutor("RemoveExpiredSessions", true,
				REFRESH_SESSIONS_TIMER_MINUTES, REFRESH_SESSIONS_TIMER_MINUTES, TimeUnit.MINUTES, () -> {
					Instant now = Instant.now();
					sessionsCache.keySet().removeIf(jwt -> !sessionsCache.get(jwt).persistent
							&& now.isAfter(sessionsCache.get(jwt).expiration));
					killedSessions.keySet().removeIf(jwt -> !killedSessions.get(jwt).persistent
							&& now.isAfter(killedSessions.get(jwt).expiration));
				});
	}

	@Override
	public PuiUserInfo loginUser(LoginData loginData)
			throws PuiCommonIncorrectUserPasswordException, PuiCommonIncorrectLoginException,
			PuiCommonAnonymousNotAllowedException, PuiCommonUserNotExistsException, PuiCommonUserDisabledException {
		if (StringUtils.isEmpty(loginData.getUsr())) {
			throw new PuiCommonIncorrectUserPasswordException();
		}

		Authentication auth = doUserValidation(loginData);
		return buildUserInfo(loginData, auth);
	}

	protected PuiUserInfo buildUserInfo(LoginData loginData, Authentication auth) {
		PuiUserSession session = (PuiUserSession) auth.getPrincipal();
		session.setIp(loginData.getIp());
		session.setUserAgent(loginData.getUserAgent());
		session.setTimezone(loginData.getJavaTimezone());
		session.setPersistent(loginData.isPersistent());
		session.setClient(loginData.getClient());

		buildJwt(session);
		sessionsCache.put(session.getJwt(),
				new SessionData(auth, session.getExpiration(), session.getCreation(), session.isPersistent()));
		try {
			setSession(session.getJwt(), true);
		} catch (PuiCommonNoSessionException | PuiCommonUserSessionTimeoutException e) {
			// should never occur, because the session is recently created
		}

		PuiUserInfo pui = new PuiUserInfo(session.getUsr(), session.getName(), session.getLanguage().getIsocode(),
				session.getEmail(), session.getDateformat(), TOKEN_BEARER_PREFIX + session.getJwt(),
				session.getLastLoginTime(), session.getLastLoginIp(), session.getProfiles(),
				session.getFunctionalities());

		session.getProperties().forEach(pui::addProperty);

		return pui;
	}

	protected Authentication doUserValidation(LoginData loginData)
			throws PuiCommonUserNotExistsException, PuiCommonIncorrectLoginException, PuiCommonUserDisabledException,
			PuiCommonAnonymousNotAllowedException, PuiCommonIncorrectUserPasswordException {
		try {
			UsernamePasswordAuthenticationToken upat = new UsernamePasswordAuthenticationToken(loginData.getUsr(),
					loginData.getPassword());
			return authenticationManager.authenticate(upat);
		} catch (UsernameNotFoundException e) {
			throw new PuiCommonUserNotExistsException(loginData.getUsr());
		} catch (DisabledException e) {
			throw new PuiCommonUserDisabledException(loginData.getUsr());
		} catch (LockedException e) {
			throw new PuiCommonAnonymousNotAllowedException();
		} catch (BadCredentialsException e) {
			throw new PuiCommonIncorrectUserPasswordException();
		} catch (AuthenticationException e) {
			throw new PuiCommonIncorrectLoginException(e);
		}
	}

	@Override
	public PuiUserSession logoutUser(String jwt) throws PuiCommonNoSessionException {
		if (StringUtils.isEmpty(jwt)) {
			throw new PuiCommonNoSessionException();
		}

		jwt = jwt.replace(TOKEN_BEARER_PREFIX, "");
		SessionData sessionData = sessionsCache.remove(jwt);
		killedSessions.put(jwt, sessionData);
		removeSession();
		return sessionData != null ? (PuiUserSession) sessionData.authentication.getPrincipal() : null;
	}

	@Override
	public void setSession(String jwt, boolean lauchNewSessionEvent)
			throws PuiCommonNoSessionException, PuiCommonUserSessionTimeoutException {
		if (StringUtils.isEmpty(jwt)) {
			throw new PuiCommonNoSessionException();
		}

		jwt = jwt.replace(TOKEN_BEARER_PREFIX, "");

		SessionData sessionData = sessionsCache.get(jwt);
		boolean recovered = false;
		if (sessionData == null) {
			sessionData = tryRecoverySession(jwt);
			if (sessionData == null) {
				throw new PuiCommonNoSessionException();
			}
			recovered = true;
		}

		checkExpirationTime(jwt);
		SecurityContextHolder.getContext().setAuthentication(sessionData.authentication);
		if (recovered || lauchNewSessionEvent) {
			// launch the session created Event here, once the session object is created and
			// setted to the SecurityContextHolder (don't do it within setSession method,
			// because this method is called each time a request arrives to the server)
			eventLauncher.fireSync(new SessionCreatedEvent((PuiUserSession) sessionData.authentication.getPrincipal()));
		}
	}

	/**
	 * Try to recovery the session using the provided JWT. Useful in case the server
	 * was restarted
	 * 
	 * @param jwt The JWT
	 * @return The session, if could be retrieved
	 */
	@SuppressWarnings("unchecked")
	private synchronized SessionData tryRecoverySession(String jwt) {
		if (killedSessions.containsKey(jwt)) {
			return null;
		}

		SessionData sessionData = sessionsCache.get(jwt);
		if (sessionData != null) {
			return sessionData;
		}

		try {
			String jwtSecret = variableService.getVariable(PuiVariableValues.SESSION_JWT_SECRET.name());
			Jwt<DefaultJwsHeader, DefaultClaims> jwtParsed = Jwts.parser().setSigningKey(jwtSecret).parse(jwt);
			String usr = jwtParsed.getBody().getSubject();
			PuiUserSession session = (PuiUserSession) userDetailsService.loadUserByUsername(usr);
			UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(session, null,
					new NullAuthoritiesMapper().mapAuthorities(session.getAuthorities()));

			if (jwtParsed.getBody().containsKey(JWT_CLAIM_TIMEZONE)) {
				session.setTimezone(ZoneId.of(jwtParsed.getBody().get(JWT_CLAIM_TIMEZONE, String.class)));
			}
			if (jwtParsed.getBody().containsKey(JWT_CLAIM_PERSISTENT)) {
				session.setPersistent(jwtParsed.getBody().get(JWT_CLAIM_PERSISTENT, Boolean.class));
			}
			if (jwtParsed.getBody().containsKey(JWT_CLAIM_IP)) {
				session.setIp(jwtParsed.getBody().get(JWT_CLAIM_IP, String.class));
			}
			if (jwtParsed.getBody().containsKey(JWT_CLAIM_USER_AGENT)) {
				session.setUserAgent(jwtParsed.getBody().get(JWT_CLAIM_USER_AGENT, String.class));
			}
			if (jwtParsed.getBody().containsKey(JWT_CLAIM_CLIENT)) {
				session.setClient(jwtParsed.getBody().get(JWT_CLAIM_CLIENT, String.class));
			}

			session.setJwt(jwt);
			session.setCreation(jwtParsed.getBody().getIssuedAt().toInstant());
			session.setExpiration(session.getCreation().plus(getTimeLogout(), ChronoUnit.MINUTES));

			sessionsCache.put(jwt,
					new SessionData(auth, session.getExpiration(), session.getCreation(), session.isPersistent()));

			checkExpirationTime(jwt);

			return sessionsCache.get(jwt);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public void removeSession() {
		SecurityContextHolder.getContext().setAuthentication(null);
	}

	@Override
	public List<PuiUserSession> getAllSessions() {
		List<PuiUserSession> list = new ArrayList<>();
		sessionsCache
				.forEach((jwt, sessionData) -> list.add((PuiUserSession) sessionData.authentication.getPrincipal()));
		return list;
	}

	/**
	 * Build the JWT token based on the logged user
	 * 
	 * @return The JWT token
	 */
	private void buildJwt(PuiUserSession session) {
		String jwtSecret = variableService.getVariable(PuiVariableValues.SESSION_JWT_SECRET.name());
		Instant creation = Instant.now();
		Instant expiration = null;
		if (!session.isPersistent() && getTimeLogout() != null) {
			expiration = creation.plus(getTimeLogout(), ChronoUnit.MINUTES);
		}

		JwtBuilder builder = Jwts.builder();
		builder.setIssuer(JWT_ISSUER);
		builder.setIssuedAt(PuiDateUtil.instantToDate(creation));
		builder.setId(UUID.randomUUID().toString());
		builder.setSubject(session.getUsr());
		builder.claim(JWT_CLAIM_TIMEZONE, session.getTimezone().getId());
		builder.claim(JWT_CLAIM_PERSISTENT, session.isPersistent());
		builder.claim(JWT_CLAIM_IP, session.getIp());
		builder.claim(JWT_CLAIM_USER_AGENT, session.getUserAgent());
		builder.claim(JWT_CLAIM_CLIENT, session.getClient());

		builder.signWith(SignatureAlgorithm.HS512, jwtSecret);

		String jwt = builder.compact();

		session.setJwt(jwt);
		session.setCreation(creation);
		session.setExpiration(expiration);
	}

	/**
	 * Check the expiration time of the given JWT token
	 * 
	 * @param jwt The JWT token
	 * @throws PuiCommonUserSessionTimeoutException If the given authorization token
	 *                                              is expired
	 */
	private void checkExpirationTime(String jwt) throws PuiCommonUserSessionTimeoutException {
		// check expiration time
		SessionData sessionData = sessionsCache.get(jwt);
		if (sessionData.persistent) {
			return;
		}

		// if no expiration time, don't check the following
		if (sessionData.expiration != null) {
			if (Instant.now().isAfter(sessionData.expiration)) {
				sessionsCache.remove(jwt);
				throw new PuiCommonUserSessionTimeoutException();
			}
			sessionData.lastUse = Instant.now();
			sessionData.expiration = getTimeLogout() != null
					? sessionData.lastUse.plus(getTimeLogout(), ChronoUnit.MINUTES)
					: null;
		}
	}

	/**
	 * Get the minutes to invalidate a session (from the PuiVariable table)
	 * 
	 * @return The number of minutes to invalidate a session
	 */
	private Long getTimeLogout() {
		Long timelogout;
		try {
			timelogout = variableService.getVariable(Long.class, PuiVariableValues.SESSION_TIMEOUT.name());
		} catch (Exception e) {
			timelogout = null;
		}
		return timelogout;
	}

	private class SessionData {
		Authentication authentication;
		Instant expiration;
		Instant lastUse;
		boolean persistent;

		public SessionData(Authentication authentication, Instant expiration, Instant lastUse, boolean persistent) {
			this.authentication = authentication;
			this.expiration = expiration;
			this.lastUse = lastUse;
			this.persistent = persistent;
		}

	}

}

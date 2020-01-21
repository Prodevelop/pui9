package es.prodevelop.pui9.login;

import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import es.prodevelop.pui9.components.PuiApplicationContext;
import es.prodevelop.pui9.enums.Pui9KnownClients;
import es.prodevelop.pui9.eventlistener.EventLauncher;
import es.prodevelop.pui9.eventlistener.event.ModifySessionPropertyEvent;
import es.prodevelop.pui9.utils.IPuiObject;
import es.prodevelop.pui9.utils.PuiLanguage;

/**
 * This class is a representation of a session. Contains the necessary
 * information for the session initialized by the user in the application.<br>
 * <br>
 * This class inherits from {@link UserDetails} and {@link CredentialsContainer}
 * from Spring Security to provide necessary methods for managing the sessions
 * automatically.
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class PuiUserSession implements IPuiObject, UserDetails, CredentialsContainer {

	private static final long serialVersionUID = 1L;

	/**
	 * Get the current session of the user that is executing the current request
	 * 
	 * @return The current user session
	 */
	public static PuiUserSession getCurrentSession() {
		return SecurityContextHolder.getContext().getAuthentication() != null
				? (PuiUserSession) SecurityContextHolder.getContext().getAuthentication().getPrincipal()
				: null;
	}

	private String usr;
	private transient String password;
	private String name;
	private PuiLanguage language;
	private String email;
	private String jwt;
	private Instant creation;
	private Instant expiration;
	private List<String> profiles = new ArrayList<>();
	private boolean persistent;
	private String ip;
	private String userAgent;
	private String client;
	private String dateformat;
	private Instant lastLoginTime;
	private String lastLoginIp;
	private Map<String, Object> properties = new HashMap<>();
	private transient List<PuiUserSessionProfile> fullProfiles = new ArrayList<>();
	private transient List<PuiUserSessionFunctionality> fullFunctionalities = new ArrayList<>();
	private transient List<String> functionalities = new ArrayList<>();
	private transient List<GrantedAuthority> authorities = new ArrayList<>();
	private transient boolean disabled;
	private transient boolean accountExpired;
	private transient boolean accountLocked;
	private transient boolean credentialsExpired;
	private transient ZoneId timezone;

	/**
	 * Creates a new session object with the given parameters
	 * 
	 * @param usr                The user unique name
	 * @param name               The complete name of the user
	 * @param password           The password of the user (in plain)
	 * @param language           The preferred language of the user
	 * @param email              The email of the user
	 * @param dateformat         The dateformat of the user
	 * @param disabled           If the user account is disabled or not
	 * @param accountExpired     If the user account is expired or not
	 * @param accountLocked      If the user account is locked or not
	 * @param credentialsExpired If the user credentials are expired or not
	 * @param lastLoginTime      The last login time
	 * @param lastLoginIp        The last login ip
	 * @param profiles           The list of assigned profiles
	 * @param functionalities    The list of assigned functionalities
	 */
	public PuiUserSession(String usr, String name, String password, PuiLanguage language, String email,
			String dateformat, boolean disabled, boolean accountExpired, boolean accountLocked,
			boolean credentialsExpired, Instant lastLoginTime, String lastLoginIp, List<PuiUserSessionProfile> profiles,
			List<PuiUserSessionFunctionality> functionalities) {
		this.usr = usr;
		this.name = name;
		this.password = password;
		this.language = language;
		this.email = email;
		this.dateformat = dateformat;
		this.disabled = disabled;
		this.accountExpired = accountExpired;
		this.accountLocked = accountLocked;
		this.credentialsExpired = credentialsExpired;
		this.lastLoginTime = lastLoginTime;
		this.lastLoginIp = lastLoginIp;

		setFullProfiles(profiles);
		setFullFunctionalities(functionalities);
	}

	/**
	 * Get the user unique name
	 * 
	 * @return The user unique name
	 */
	public String getUsr() {
		return usr;
	}

	/**
	 * Get the complete user name
	 * 
	 * @return The complete user name
	 */
	public String getName() {
		return name;
	}

	@Override
	public String getPassword() {
		return password;
	}

	/**
	 * Check if the session should be persistent or not
	 * 
	 * @return true if the session is persistent; false if not
	 */
	public boolean isPersistent() {
		return persistent;
	}

	/**
	 * Set if the session should be persistent or not
	 * 
	 * @param persistent
	 */
	public void setPersistent(boolean persistent) {
		this.persistent = persistent;
	}

	/**
	 * Get the prefered language of the user for this session
	 * 
	 * @return The language
	 */
	public PuiLanguage getLanguage() {
		return language;
	}

	/**
	 * Set the prefered language for this session
	 * 
	 * @param language The new language
	 */
	public void setLanguage(PuiLanguage language) {
		this.language = language;
	}

	/**
	 * Get the email of the user
	 * 
	 * @return The email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Get the dateformat of the user
	 * 
	 * @return The dateformat
	 */
	public String getDateformat() {
		return dateformat;
	}

	/**
	 * Check if the user is disabled or not
	 * 
	 * @return True if disabled; false if not
	 */
	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	/**
	 * Get the list of profiles
	 * 
	 * @return The list of profiles
	 */
	public List<PuiUserSessionProfile> getFullProfiles() {
		return fullProfiles;
	}

	private void setFullProfiles(List<PuiUserSessionProfile> profiles) {
		Collections.sort(profiles);
		this.fullProfiles.clear();
		this.fullProfiles.addAll(profiles);
		setProfiles(profiles);
	}

	/**
	 * Get the list of functionalities
	 * 
	 * @return The list of functionalities
	 */
	public List<PuiUserSessionFunctionality> getFullFunctionalities() {
		return fullFunctionalities;
	}

	public void setFullFunctionalities(List<PuiUserSessionFunctionality> functionalities) {
		Collections.sort(functionalities);
		this.fullFunctionalities.clear();
		this.fullFunctionalities.addAll(functionalities);
		setFunctionalities(functionalities);
	}

	/**
	 * Get the list of assigned profiles
	 * 
	 * @return The list of profiles
	 */
	public List<String> getProfiles() {
		return profiles;
	}

	/**
	 * Set the profiles of the user
	 * 
	 * @param profiles
	 */
	public void setProfiles(List<PuiUserSessionProfile> profiles) {
		Collections.sort(profiles);
		this.profiles.clear();
		profiles.forEach(p -> this.profiles.add(p.getProfile()));
	}

	/**
	 * Get the list of assigned functionalities
	 * 
	 * @return The list of functionalities
	 */
	public List<String> getFunctionalities() {
		return functionalities;
	}

	public void setFunctionalities(List<PuiUserSessionFunctionality> functionalities) {
		Collections.sort(functionalities);
		this.functionalities.clear();
		this.authorities.clear();
		functionalities.forEach(f -> {
			if (!this.functionalities.contains(f.getFunctionality())) {
				this.functionalities.add(f.getFunctionality());
				this.authorities.add(new SimpleGrantedAuthority(f.getFunctionality()));
			}
		});
	}

	/**
	 * Get the JWT token assigned for this session. The JWT token may change if the
	 * session is extended automatically
	 * 
	 * @return
	 */
	public String getJwt() {
		return jwt;
	}

	/**
	 * Modify the JWT token for this session. Typically when the session is extended
	 * automatically
	 * 
	 * @param jwt The new JWT session
	 */
	public void setJwt(String jwt) {
		this.jwt = jwt;
	}

	/**
	 * Get the creation time of the session
	 * 
	 * @return The creation time
	 */
	public Instant getCreation() {
		return creation;
	}

	/**
	 * Set the creation time of the session
	 * 
	 * @param creation The creation time
	 */
	public void setCreation(Instant creation) {
		this.creation = creation;
	}

	/**
	 * Get the expiration time of the session
	 * 
	 * @return The expiration time
	 */
	public Instant getExpiration() {
		return expiration;
	}

	/**
	 * Set the expiration time of the session
	 * 
	 * @param expiration The expiration time
	 */
	public void setExpiration(Instant expiration) {
		this.expiration = expiration;
	}

	/**
	 * Get the IP of the client
	 * 
	 * @return The IP of the client
	 */
	public String getIp() {
		return ip;
	}

	/**
	 * Set the IP of the client
	 * 
	 * @param ip The IP of the client
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}

	/**
	 * Get the User Agent of the client
	 * 
	 * @return The User Agent of the client
	 */
	public String getUserAgent() {
		return userAgent;
	}

	/**
	 * Set the User Agent of the client
	 * 
	 * @param userAgent The User Agent of the client
	 */
	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	/**
	 * Get the client name
	 * 
	 * @return The client name
	 */
	public String getClient() {
		return client;
	}

	/**
	 * Set the client name
	 * 
	 * @param client The client name
	 */
	public void setClient(String client) {
		this.client = client;
	}

	/**
	 * Get the time zone of the session
	 * 
	 * @return The Zone Id
	 */
	public ZoneId getTimezone() {
		if (timezone != null) {
			return timezone;
		} else {
			return ZoneId.systemDefault();
		}
	}

	/**
	 * Set the time zone for this session
	 * 
	 * @param timezone The zone Id
	 */
	public void setTimezone(ZoneId timezone) {
		this.timezone = timezone;
	}

	@Override
	public String getUsername() {
		return getUsr();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public boolean isAccountNonExpired() {
		return !accountExpired;
	}

	public void setAccountExpired(boolean accountExpired) {
		this.accountExpired = accountExpired;
	}

	@Override
	public boolean isAccountNonLocked() {
		return !accountLocked;
	}

	public void setAccountLocked(boolean accountLocked) {
		this.accountLocked = accountLocked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return !credentialsExpired;
	}

	public void setCredentialsExpired(boolean credentialsExpired) {
		this.credentialsExpired = credentialsExpired;
	}

	@Override
	public boolean isEnabled() {
		return !disabled;
	}

	@Override
	public void eraseCredentials() {
		password = null;
		authorities.clear();
	}

	public Instant getLastLoginTime() {
		return lastLoginTime;
	}

	public String getLastLoginIp() {
		return lastLoginIp;
	}

	public void addProperty(String property, Object value) {
		Object oldValue = properties.put(property, value);
		PuiApplicationContext.getInstance().getBean(EventLauncher.class)
				.fireSync(new ModifySessionPropertyEvent(property, oldValue, value));
	}

	public Map<String, Object> getProperties() {
		return properties;
	}

	@SuppressWarnings("unchecked")
	public <T> T getProperty(String property) {
		return (T) properties.get(property);
	}

	public boolean isPui9Client() {
		return Objects.equals(client, Pui9KnownClients.PUI9_CLIENT.name());
	}

	public static class PuiUserSessionProfile implements Comparable<PuiUserSessionProfile> {
		private String profile;

		public static PuiUserSessionProfile createNewProfile(String profile) {
			PuiUserSessionProfile prof = new PuiUserSessionProfile();
			prof.profile = profile;
			return prof;
		}

		public String getProfile() {
			return profile;
		}

		@Override
		public String toString() {
			return profile;
		}

		@Override
		public int compareTo(PuiUserSessionProfile o) {
			return profile.compareTo(o.getProfile());
		}
	}

	public static class PuiUserSessionFunctionality implements Comparable<PuiUserSessionFunctionality> {
		private String profile;
		private String functionality;

		public static PuiUserSessionFunctionality createNewFunctionality(String profile, String functionality) {
			PuiUserSessionFunctionality func = new PuiUserSessionFunctionality();
			func.profile = profile;
			func.functionality = functionality;
			return func;
		}

		public String getProfile() {
			return profile;
		}

		public String getFunctionality() {
			return functionality;
		}

		@Override
		public String toString() {
			return functionality + " (" + profile + ")";
		}

		@Override
		public int compareTo(PuiUserSessionFunctionality o) {
			return functionality.compareTo(o.getFunctionality());
		}
	}

}

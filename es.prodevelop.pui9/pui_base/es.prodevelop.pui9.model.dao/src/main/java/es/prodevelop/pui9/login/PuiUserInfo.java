package es.prodevelop.pui9.login;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.userdetails.UserDetails;

import es.prodevelop.pui9.utils.IPuiObject;

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
public class PuiUserInfo implements IPuiObject {

	private static final long serialVersionUID = 1L;

	private String usr;
	private String name;
	private String language;
	private String email;
	private String dateformat;
	private String jwt;
	private Instant lastLoginTime;
	private String lastLoginIp;
	private List<String> profiles = new ArrayList<>();
	private List<String> functionalities = new ArrayList<>();
	private Map<String, Object> properties = new HashMap<>();

	public PuiUserInfo(String usr, String name, String language, String email, String dateformat, String jwt,
			Instant lastLoginTime, String lastLoginIp, List<String> profiles, List<String> functionalities) {
		this.usr = usr;
		this.name = name;
		this.language = language;
		this.email = email;
		this.dateformat = dateformat;
		this.jwt = jwt;
		this.lastLoginTime = lastLoginTime;
		this.lastLoginIp = lastLoginIp;
		this.profiles = profiles;
		this.functionalities = functionalities;
	}

	public String getUsr() {
		return usr;
	}

	public String getName() {
		return name;
	}

	public String getLanguage() {
		return language;
	}

	public String getEmail() {
		return email;
	}

	public String getDateformat() {
		return dateformat;
	}

	public String getJwt() {
		return jwt;
	}

	public Instant getLastLoginTime() {
		return lastLoginTime;
	}

	public String getLastLoginIp() {
		return lastLoginIp;
	}

	public List<String> getProfiles() {
		return profiles;
	}

	public void setProfiles(List<String> profiles) {
		this.profiles = profiles;
	}

	public List<String> getFunctionalities() {
		return functionalities;
	}

	public void addProperty(String property, Object value) {
		properties.put(property, value);
	}

}

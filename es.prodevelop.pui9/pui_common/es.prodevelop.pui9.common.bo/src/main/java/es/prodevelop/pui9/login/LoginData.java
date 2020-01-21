package es.prodevelop.pui9.login;

import java.time.ZoneId;

import es.prodevelop.pui9.utils.PuiDateUtil;
import io.swagger.annotations.ApiModelProperty;

public class LoginData {

	@ApiModelProperty(position = 0, value = "The user (in plain)", required = true)
	private String usr;
	@ApiModelProperty(position = 1, value = "The password (in plain)", required = true)
	private String password;
	@ApiModelProperty(position = 2, value = "Persistent session", required = false)
	private boolean persistent;
	@ApiModelProperty(position = 3, value = "The client name", required = false)
	private String client;
	@ApiModelProperty(hidden = true, position = 4, value = "The client zone offset (in minutes)")
	private String timezone;
	@ApiModelProperty(hidden = true)
	private String ip;
	@ApiModelProperty(hidden = true)
	private String userAgent;

	public LoginData(String usr, String password, boolean persistent, String ip, String userAgent, String timezone,
			String client) {
		this.usr = usr;
		this.password = password;
		this.persistent = persistent;
		this.ip = ip;
		this.userAgent = userAgent;
		this.timezone = timezone;
		this.client = client;
	}

	public String getUsr() {
		return usr;
	}

	public void setUsr(String usr) {
		this.usr = usr;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isPersistent() {
		return persistent;
	}

	public void setPersistent(boolean persistent) {
		this.persistent = persistent;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public String getTimezone() {
		return timezone;
	}

	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}

	public String getClient() {
		return client;
	}

	public void setClient(String client) {
		this.client = client;
	}

	@ApiModelProperty(hidden = true)
	public ZoneId getJavaTimezone() {
		return timezone != null ? ZoneId.of(timezone) : PuiDateUtil.utcZone;
	}

}
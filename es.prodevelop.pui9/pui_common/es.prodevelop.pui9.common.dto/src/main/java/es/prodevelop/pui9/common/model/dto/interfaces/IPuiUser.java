package es.prodevelop.pui9.common.model.dto.interfaces;

import java.util.List;

/**
 * @generated
 */
public interface IPuiUser extends IPuiUserPk {
	/**
	 * @generated
	 */
	String NAME_COLUMN = "name";
	/**
	 * @generated
	 */
	String NAME_FIELD = "name";
	/**
	 * @generated
	 */
	String PASSWORD_COLUMN = "password";
	/**
	 * @generated
	 */
	String PASSWORD_FIELD = "password";
	/**
	 * @generated
	 */
	String LANGUAGE_COLUMN = "language";
	/**
	 * @generated
	 */
	String LANGUAGE_FIELD = "language";
	/**
	 * @generated
	 */
	String EMAIL_COLUMN = "email";
	/**
	 * @generated
	 */
	String EMAIL_FIELD = "email";
	/**
	 * @generated
	 */
	String DISABLED_COLUMN = "disabled";
	/**
	 * @generated
	 */
	String DISABLED_FIELD = "disabled";
	/**
	 * @generated
	 */
	String DISABLED_DATE_COLUMN = "disabled_date";
	/**
	 * @generated
	 */
	String DISABLED_DATE_FIELD = "disableddate";
	/**
	 * @generated
	 */
	String DATEFORMAT_COLUMN = "dateformat";
	/**
	 * @generated
	 */
	String DATEFORMAT_FIELD = "dateformat";
	/**
	 * @generated
	 */
	String RESET_PASSWORD_TOKEN_COLUMN = "reset_password_token";
	/**
	 * @generated
	 */
	String RESET_PASSWORD_TOKEN_FIELD = "resetpasswordtoken";
	/**
	 * @generated
	 */
	String LAST_ACCESS_TIME_COLUMN = "last_access_time";
	/**
	 * @generated
	 */
	String LAST_ACCESS_TIME_FIELD = "lastaccesstime";
	/**
	 * @generated
	 */
	String LAST_ACCESS_IP_COLUMN = "last_access_ip";
	/**
	 * @generated
	 */
	String LAST_ACCESS_IP_FIELD = "lastaccessip";

	/**
	 * @generated
	 */
	String getName();

	/**
	 * @generated
	 */
	void setName(String name);

	/**
	 * @generated
	 */
	String getPassword();

	/**
	 * @generated
	 */
	void setPassword(String password);

	/**
	 * @generated
	 */
	String getLanguage();

	/**
	 * @generated
	 */
	void setLanguage(String language);

	/**
	 * @generated
	 */
	String getEmail();

	/**
	 * @generated
	 */
	void setEmail(String email);

	/**
	 * @generated
	 */
	Integer getDisabled();

	/**
	 * @generated
	 */
	void setDisabled(Integer disabled);

	/**
	 * @generated
	 */
	java.time.Instant getDisableddate();

	/**
	 * @generated
	 */
	void setDisableddate(java.time.Instant disableddate);

	/**
	 * @generated
	 */
	String getDateformat();

	/**
	 * @generated
	 */
	void setDateformat(String dateformat);

	/**
	 * @generated
	 */
	String getResetpasswordtoken();

	/**
	 * @generated
	 */
	void setResetpasswordtoken(String resetpasswordtoken);

	/**
	 * @generated
	 */
	java.time.Instant getLastaccesstime();

	/**
	 * @generated
	 */
	void setLastaccesstime(java.time.Instant lastaccesstime);

	/**
	 * @generated
	 */
	String getLastaccessip();

	/**
	 * @generated
	 */
	void setLastaccessip(String lastaccessip);

	String PROFILES_FIELD = "profiles";

	List<IPuiProfile> getProfiles();

	void setProfiles(List<IPuiProfile> profiles);
}

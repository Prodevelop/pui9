package es.prodevelop.pui9.common.model.views.dto.interfaces;

import es.prodevelop.pui9.model.dto.interfaces.IViewDto;

/**
 * @generated
 */
public interface IVPuiUser extends IViewDto {
	/**
	 * @generated
	 */
	String USR_COLUMN = "usr";
	/**
	 * @generated
	 */
	String USR_FIELD = "usr";
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
	String EMAIL_COLUMN = "email";
	/**
	 * @generated
	 */
	String EMAIL_FIELD = "email";
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
	String DATEFORMAT_COLUMN = "dateformat";
	/**
	 * @generated
	 */
	String DATEFORMAT_FIELD = "dateformat";
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
	String getUsr();

	/**
	 * @generated
	 */
	void setUsr(String usr);

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
	String getEmail();

	/**
	 * @generated
	 */
	void setEmail(String email);

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
	String getDateformat();

	/**
	 * @generated
	 */
	void setDateformat(String dateformat);

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
}

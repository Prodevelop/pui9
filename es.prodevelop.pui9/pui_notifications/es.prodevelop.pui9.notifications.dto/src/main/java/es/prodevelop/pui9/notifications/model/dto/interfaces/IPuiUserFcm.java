package es.prodevelop.pui9.notifications.model.dto.interfaces;

/**
 * @generated
 */
public interface IPuiUserFcm extends IPuiUserFcmPk {
	/**
	 * @generated
	 */
	String LAST_USE_COLUMN = "last_use";
	/**
	 * @generated
	 */
	String LAST_USE_FIELD = "lastuse";

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
	java.time.Instant getLastuse();

	/**
	 * @generated
	 */
	void setLastuse(java.time.Instant lastuse);

	/**
	 * @generated
	 */
	String USR_COLUMN = "usr";
	/**
	 * @generated
	 */
	String USR_FIELD = "usr";
}

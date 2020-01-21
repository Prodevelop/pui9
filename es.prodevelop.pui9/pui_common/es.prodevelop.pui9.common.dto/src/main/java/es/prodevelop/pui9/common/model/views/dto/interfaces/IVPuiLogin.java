package es.prodevelop.pui9.common.model.views.dto.interfaces;

import es.prodevelop.pui9.model.dto.interfaces.IViewDto;

/**
 * @generated
 */
public interface IVPuiLogin extends IViewDto {
	/**
	 * @generated
	 */
	String ID_COLUMN = "id";
	/**
	 * @generated
	 */
	String ID_FIELD = "id";
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
	String DATETIME_COLUMN = "datetime";
	/**
	 * @generated
	 */
	String DATETIME_FIELD = "datetime";
	/**
	 * @generated
	 */
	String IP_COLUMN = "ip";
	/**
	 * @generated
	 */
	String IP_FIELD = "ip";

	/**
	 * @generated
	 */
	Integer getId();

	/**
	 * @generated
	 */
	void setId(Integer id);

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
	java.time.Instant getDatetime();

	/**
	 * @generated
	 */
	void setDatetime(java.time.Instant datetime);

	/**
	 * @generated
	 */
	String getIp();

	/**
	 * @generated
	 */
	void setIp(String ip);
}

package es.prodevelop.pui9.notifications.model.dto;

import es.prodevelop.pui9.annotations.PuiField;
import es.prodevelop.pui9.enums.ColumnType;
import es.prodevelop.pui9.model.dto.AbstractTableDto;
import es.prodevelop.pui9.notifications.model.dto.interfaces.IPuiUserFcmPk;
import es.prodevelop.pui9.utils.PuiObjectUtils;

/**
 * @generated
 */
public class PuiUserFcmPk extends AbstractTableDto implements IPuiUserFcmPk {
	/**
	 * @generated
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * @generated
	 */
	@PuiField(columnname = IPuiUserFcmPk.TOKEN_COLUMN, ispk = true, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 300, islang = false, isgeometry = false, issequence = false)
	private String token;

	/**
	 * @generated
	 */
	public PuiUserFcmPk() {
	}

	/**
	 * @generated
	 */
	public PuiUserFcmPk(String token) {
		this.token = token;
	}

	/**
	 * @generated
	 */
	@Override
	public String getToken() {
		return token;
	}

	/**
	 * @generated
	 */
	@Override
	public void setToken(String token) {
		this.token = token;
	}

	/**
	 * @generated
	 */
	@Override
	@SuppressWarnings("unchecked")
	public PuiUserFcmPk createPk() {
		PuiUserFcmPk pk = new PuiUserFcmPk();
		PuiObjectUtils.copyProperties(pk, this);
		return pk;
	}
}

package es.prodevelop.pui9.common.exceptions;

public class PuiCommonImportExportDtoColumnErrorException extends AbstractPuiCommonException {

	private static final long serialVersionUID = 1L;

	public static final Integer CODE = 217;

	public PuiCommonImportExportDtoColumnErrorException(String column, Object value) {
		super(CODE, column, value);
	}

}

package es.prodevelop.pui9.common.exceptions;

public class PuiCommonImportExportInvalidColumnException extends AbstractPuiCommonException {

	private static final long serialVersionUID = 1L;

	public static final Integer CODE = 214;

	public PuiCommonImportExportInvalidColumnException(String column) {
		super(CODE, column);
	}

}
